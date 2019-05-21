package leaf.prod.app.net;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import android.net.ParseException;

import org.json.JSONException;
import com.google.gson.JsonParseException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Title:ExceptionEngine
 */

public class ExceptionEngine {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;

    private static final int FORBIDDEN = 403;

    private static final int NOT_FOUND = 404;

    private static final int REQUEST_TIMEOUT = 408;

    private static final int INTERNAL_SERVER_ERROR = 500;

    private static final int BAD_GATEWAY = 502;

    private static final int SERVICE_UNAVAILABLE = 503;

    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, String.valueOf(ERROR.HTTP_ERROR));
            //			ZgqbLogger.log(ex.getMessage());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络异常";  //均视为网络错误
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ex = new ApiException(e, String.valueOf(ERROR.PARSE_ERROR));
            ex.message = "解析错误";            //均视为解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, String.valueOf(ERROR.NETWORD_ERROR));
            ex.message = "连接失败";  //均视为网络错误
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, String.valueOf(ERROR.NETWORD_ERROR));
            ex.message = "连接超时";  //
            return ex;
        } else {
            ex = new ApiException(e, String.valueOf(ERROR.UNKNOWN));
            //			ZgqbLogger.log(ex.getMessage());
            ex.message = "网络异常";          //未知错误
            return ex;
        }
    }
}
