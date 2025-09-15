var exec = require('cordova/exec');

module.exports = {
    execAsync(func, param) {
        var args = Array.prototype.slice.call(arguments);
        return new Promise(function(resolve) {
            args.unshift('DYGame');
            args.unshift(function() {
                resolve(false);
            });
            args.unshift(function() {
                resolve(true);
            });
            exec.apply(this, args);
        });
    },

    init() {
        return this.execAsync('init');
    },

    onGameActive() {
        return this.execAsync('onGameActive');
    },

    onAccountRegister(userId) {
        return this.execAsync('onAccountRegister', [userId]);
    },
    
    onRoleRegister(userId, roleId) {
        return this.execAsync('onRoleRegister', [userId, roleId]);
    },

    onAccountLogin(userId, lastLoginTime) {
        lastLoginTime = lastLoginTime || 0;
        return this.execAsync('onAccountLogin', [userId, lastLoginTime]);
    },
    
    onRoleLogin(userId, roleId, lastLoginTime) {
        lastLoginTime = lastLoginTime || 0;
        return this.execAsync('onRoleLogin', [userId, roleId, lastLoginTime]);
    },

    onPay(userId, roleId, orderId, amount, productId, productName, productDesc) {
        roleId = roleId || '';  
        productId = productId || '';
        productName = productName || '';
        productDesc = productDesc || '';
        return this.execAsync('onPay', [userId, roleId, orderId, amount, productId, productName, productDesc]);
    },
}