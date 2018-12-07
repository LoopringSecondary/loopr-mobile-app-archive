package leaf.prod.app.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import leaf.prod.app.R;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-04 10:17 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class PasswordDialogUtil {

    private static Map<Context, AlertDialog> map = new HashMap<>();

    private static EditText passwordInput;

    public static void showPasswordDialog(Context context, View.OnClickListener listener) {
        if (map.get(context) == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_put_password, null);
            builder.setView(view);
            passwordInput = view.findViewById(R.id.password_input);
            view.findViewById(R.id.cancel).setOnClickListener(v -> {
                showKeyboard(context, false);
                map.get(context).dismiss();
            });
            view.findViewById(R.id.confirm).setOnClickListener(listener);
            builder.setCancelable(true);
            AlertDialog passwordDialog = builder.create();
            passwordDialog.setCancelable(false);
            passwordDialog.setCanceledOnTouchOutside(false);
            map.put(context, passwordDialog);
        } else {
            passwordInput.setText("");
        }
        map.get(context).show();
    }

    public static String getInputPassword() {
        return passwordInput != null ? passwordInput.getText().toString() : "";
    }

    public static void clearPassword() {
        passwordInput.setText("");
    }

    public static void dismiss(Context context) {
        if (map.get(context) != null) {
            passwordInput = null;
            map.get(context).dismiss();
            map.remove(context);
        }
    }

    private static void showKeyboard(Context context, boolean show) {
        if (passwordInput != null && map.get(context) != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) passwordInput.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                passwordInput.requestFocus();
                if (show)
                    inputMethodManager.showSoftInput(passwordInput, 0);
                else
                    inputMethodManager.hideSoftInputFromWindow(passwordInput.getWindowToken(), 0);
            }
        }
    }
}

