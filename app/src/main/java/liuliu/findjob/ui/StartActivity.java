package liuliu.findjob.ui;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.droidlover.xdroidmvp.db.FinalDb;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import liuliu.findjob.R;
import liuliu.findjob.model.CodeModel;
import liuliu.findjob.model.Config;
import liuliu.findjob.model.UpdateManage;
import liuliu.findjob.util.Utils;

/**
 * Created by Administrator on 2017/1/24.
 */

public class StartActivity extends XActivity {
    FinalDb db;

    @Override
    public void initData(Bundle savedInstanceState) {
        db = FinalDb.create(this);
        BmobQuery<UpdateManage> query = new BmobQuery<>();
        query.addWhereEqualTo("project", "1");
        query.findObjects(new FindListener<UpdateManage>() {
            @Override
            public void done(List<UpdateManage> list, BmobException e) {
                if (e == null) {
                    UpdateManage manage = list.get(0);
                    //执行更新字典操作
                    if (!Utils.getCache(Config.Code_Update_Time).equals(manage.getUpdatedAt())) {
                        if (db.findAll(CodeModel.class).size() > 0) {
                            db.deleteAll(CodeModel.class);
                        }
                        Utils.putCache(Config.Code_Update_Time, manage.getUpdatedAt());
                        BmobQuery<CodeModel> query = new BmobQuery<>();
                        query.findObjects(new FindListener<CodeModel>() {
                            @Override
                            public void done(List<CodeModel> list, BmobException e) {
                                if (e == null) {
                                    for (CodeModel model : list) {
                                        model.setObjectid(model.getObjectId());
                                        db.save(model);
                                    }
                                    checkToLoginOrMain();
                                } else {
                                    checkToLoginOrMain();
                                }
                            }
                        });
                    } else {
                        checkToLoginOrMain();
                    }
                } else {
                    checkToLoginOrMain();
                }
            }
        });
    }

    /**
     * 检查跳转到登录还是主页
     */
    public void checkToLoginOrMain() {
        if (TextUtils.isEmpty(Utils.getCache(Config.KEY_User_ID))) {
            Utils.IntentPost(LoginActivity.class);
        } else {
            Utils.IntentPost(MainActivity.class);
        }
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_start;
    }

    @Override
    public Object newP() {
        return null;
    }
}
