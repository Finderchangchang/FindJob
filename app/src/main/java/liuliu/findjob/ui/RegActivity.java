package liuliu.findjob.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.view.TitleBar;
import liuliu.findjob.R;
import liuliu.findjob.model.Config;
import liuliu.findjob.util.Utils;

public class RegActivity extends XActivity {

    @BindView(R.id.toolbar)
    TitleBar toolbar;
    @BindView(R.id.tel_et)
    EditText telEt;
    @BindView(R.id.pwd_et)
    EditText pwdEt;
    @BindView(R.id.cofirm_pwd_et)
    EditText cofirmPwdEt;
    @BindView(R.id.reg_btn)
    Button regBtn;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolbar.setLeftClick(() -> finish());
        regBtn.setOnClickListener(view -> {
            String tel = telEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            String cofirm = cofirmPwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(tel)) {
                ToastShort("请输入手机号码");
            } else if (!Utils.isMobileNo(tel)) {
                ToastShort("请输入正确手机号码");
            } else if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cofirm)) {
                ToastShort("前后密码不一致请重新输入");
            } else if (!pwd.equals(cofirm)) {
                ToastShort("前后密码不一致请重新输入");
            } else {
                BmobUser bu = new BmobUser();
                bu.setUsername(tel);
                bu.setPassword(pwd);
                bu.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser s, BmobException e) {
                        if (e == null) {
                            Utils.IntentPost(RegPersonActivity.class);
                            Utils.putCache(Config.KEY_User_ID, tel);
                            LoginActivity.instance.finish();
                            finish();
                        } else {
                            ToastShort(getResources().getString(R.string.no_wang));
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_reg;
    }

    @Override
    public Object newP() {
        return null;
    }
}
