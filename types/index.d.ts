declare namespace DYGame {
    /**
     * 用户同意隐私协议后调用SDK启动接口
     */
    function init(): Promise<boolean>;

    /**
     * 游戏激活事件上报
     */
    function onGameActive(): Promise<boolean>;

    /**
     * 账号注册事件上报
     * @param userId 
     */
    function onAccountRegister(userId: string): Promise<boolean>;

    /**
     * 角色注册事件上报（可选）
     * @param userId 
     * @param roleId 
     */
    function onRoleRegister(userId: string, roleId: string): Promise<boolean>;

    /**
     * 账号登录事件上报
     * @param userId 
     * @param lastLoginTime 账户最近一次（上一次）登录时间，秒级时间戳，请传入 >= 0 的值，缺省默认传0。
     */
    function onAccountLogin(userId: string, lastLoginTime?: number): Promise<boolean>;

    /**
     * 角色登录事件上报（可选）
     * @param userId 
     * @param roleId 
     * @param lastLoginTime 角色最近一次（上一次）登录时间，秒级时间戳，请传入 >= 0 的值，缺省默认传0。
     */
    function onRoleLogin(userId: string, roleId: string, lastLoginTime?: number): Promise<boolean>;

    /**
     * 
     * @param userId 
     * @param roleId 游戏内角色唯一ID，若不回传则请填空串""
     * @param orderId 订单唯一标记
     * @param amount 订单支付金额，单位为分，需要 > 0
     * @param productId 商品ID
     * @param productName 商品名称
     * @param productDesc 商品描述
     */
    function onPay(
        userId: string, 
        roleId: string,
        orderId: string, 
        amount: number, 
        productId?: string, 
        productName?: string, 
        productDesc?: string
    ): Promise<boolean>;
}