package com.tomcat360.lyqb.service;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Title:UserService
 * Package:com.tomcat360.Service
 * Description:用户相关service
 * Author: wwh@tomcat360.com
 * Date: 16/10/20
 * Version: V1.0.0
 * 版本号修改日期修改人修改内容
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
