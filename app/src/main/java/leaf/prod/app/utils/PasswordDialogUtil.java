package leaf.prod.app.utils;

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

    private static AlertDialog passwordDialog;

    private static EditText passwordInput;

    public static void showPasswordDialog(Context context, View.OnClickListener listener) {
        if (passwordDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_put_password, null);
            builder.setView(view);
            passwordInput = view.findViewById(R.id.password_input);
            view.findViewById(R.id.cancel).setOnClickListener(v -> {
                showKeyboard(false);
                passwordDialog.dismiss();
            });
            view.findViewById(R.id.confirm).setOnClickListener(listener);
            builder.setCancelable(true);
            passwordDialog = null;
            passwordDialog = builder.create();
            passwordDialog.setCancelable(false);
            passwordDialog.setCanceledOnTouchOutside(false);
        } else {
            passwordInput.setText("");
        }
        passwordDialog.show();
    }

    public static String getInputPassword() {
        return passwordInput != null ? passwordInput.getText().toString() : "";
    }

    private static void showKeyboard(boolean show) {
        if (passwordInput != null && passwordDialog != null) {
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

