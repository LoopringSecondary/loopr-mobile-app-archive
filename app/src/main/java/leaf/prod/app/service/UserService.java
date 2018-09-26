package leaf.prod.app.service;

/**
 * Title:UserService
 */

public interface UserService {
    //    /**
    //     * 用户获取短信验证码
    //     *
    //     * @param phone 手机号
    //     * @param flag  标志0注册；1：忘记密码
    //     * @return Observable<HttpResponse>
    //     */
    //
    //    @FormUrlEncoded
    //    @POST("vcode")
    //    Observable<BaseStringNewResponse<UserInfo>> getMessageCode(@Field("phone") String phone, @Field("flag") int flag);
    //
    //    /**
    //     * 忘记密码
    //     *
    //     * @param phone
    //     * @param newPassword
    //     * @param vcode
    //     * @return
    //     */
    //    @FormUrlEncoded
    //    @POST("updatePassword")
    //    Observable<BaseResponse> doFindPassword(@Field("phone") String phone, @Field("newPassword") String newPassword, @Field("vcode") String vcode, @Field("token") String token);
}
