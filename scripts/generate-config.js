#!/usr/bin/env node
const fs = require('fs');
const path = require('path');

/**
 * 此脚本用于动态创建sdk所使用的配置json，删除插件时亦会自动删除
 */

/**
 * 配置文件模板
 */
const configTemplate = {
    "app_id": "123456",
    "screen_orientation": "sensorLandscape",
    "osdk_type": "Omnichannel_ActivityLink",
    "client_key": {
        "douyin_key": "abcdef"
    },
    "debug_mode": false
}

/**
 * 从控制台命令读取传入的参数
 * @param {string} cmdLine 
 * @returns 
 */
function parseVarsFromCmdLine(cmdLine) {
    const out = {};
    if (!cmdLine || typeof cmdLine !== 'string') return out;

    // 支持：--variable NAME=value   或   --variable=NAME=value
    // value 支持 "..." 或 '...'，也支持不带引号的连续字符
    const re = /--variable(?:\s+|=)\s*([A-Za-z0-9_]+)=("([^"\\]|\\.)*"|'([^'\\]|\\.)*'|[^\s]+)/g;
    let m;
    while ((m = re.exec(cmdLine)) !== null) {
        const key = m[1];
        let raw = m[2] || '';
        // 去掉首尾引号并做简单反斜杠还原
        if ((raw.startsWith('"') && raw.endsWith('"')) || (raw.startsWith("'") && raw.endsWith("'"))) {
            raw = raw.slice(1, -1).replace(/\\(["'])/g, '$1');
        }
        out[key] = raw;
    }
    return out;
}

module.exports = function (context) {
    const platforms = context.opts.cordova.platforms;
    const projectRoot = context.opts.projectRoot;

    const isUninstall = context.hook === 'before_plugin_uninstall';

    if (!isUninstall) {
        const vars = parseVarsFromCmdLine(context.cmdLine || '');
        if (!vars.APP_ID || !vars.CLIENT_KEY) {
            console.error('APP_ID and CLIENT_KEY are required');
            return;
        }
        configTemplate.app_id = vars.APP_ID;
        configTemplate.client_key.douyin_key = vars.CLIENT_KEY;
        if (vars.DEBUG_MODE === 'true') {
            configTemplate.debug_mode = true;
        }
        if (vars.SCREEN_ORIENTATION) {
            configTemplate.screen_orientation = vars.SCREEN_ORIENTATION;
        }
        if (vars.OSDK_TYPE) {
            configTemplate.osdk_type = vars.OSDK_TYPE;
        }
        console.log('[cordova-plugin-dygame] config: \n', JSON.stringify(configTemplate, null, 2));
    }

    const ensureDir = (dir) => fs.mkdirSync(dir, { recursive: true });
    const writeJson = (file, obj) => {
        ensureDir(path.dirname(file));
        fs.writeFileSync(file, JSON.stringify(obj, null, 2), 'utf8');
        console.log('[cordova-plugin-dygame] write config: ', file);
    };
    const removeFile = (file) => {
        try { fs.unlinkSync(file); console.log('[cordova-plugin-dygame] remove config: ', file); } catch (_) { }
    };

    if (platforms.includes('ios')) {
        const iosWww = path.join(projectRoot, 'platforms', 'ios', 'www');
        const iosFile = path.join(iosWww, 'UOPSDKConfig.json');

        if (isUninstall) {
            removeFile(iosFile);
        } else {
            writeJson(iosFile, configTemplate);
        }
    }

    if (platforms.includes('android')) {
        const androidRoot = path.join(projectRoot, 'platforms', 'android');
        const assetsDirV10 = path.join(androidRoot, 'app', 'src', 'main', 'assets');
        const assetsDirOld = path.join(androidRoot, 'src', 'main', 'assets'); // 兜底
        const assetsDir = fs.existsSync(path.join(androidRoot, 'app')) ? assetsDirV10 : assetsDirOld;
        const androidFile = path.join(assetsDir, 'config.json');

        if (isUninstall) {
            removeFile(androidFile);
        } else {
            writeJson(androidFile, configTemplate);
        }
    }
}