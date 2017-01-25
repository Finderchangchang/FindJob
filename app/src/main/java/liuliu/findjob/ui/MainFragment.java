package liuliu.findjob.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.droidlover.xdroidmvp.db.FinalDb;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import liuliu.findjob.R;
import liuliu.findjob.base.StateView;
import liuliu.findjob.model.ArticleModel;
import liuliu.findjob.model.CodeModel;
import liuliu.findjob.model.Config;
import liuliu.findjob.model.TypeOfWorkModel;
import liuliu.findjob.model.UserInfo;
import liuliu.findjob.pager.PBasePager;
import liuliu.findjob.util.Utils;
import liuliu.findjob.view.CategoryAdapter;
import liuliu.findjob.view.CommonAdapter;
import liuliu.findjob.view.CommonViewHolder;

/**
 * Created by Administrator on 2017/1/24.
 */

public class MainFragment extends XFragment<PBasePager> {
    int index = 0;
    @BindView(R.id.user_iv)
    @Nullable
    ImageView userIv;
    @BindView(R.id.user_name_tv)
    @Nullable
    TextView userNameTv;
    @BindView(R.id.user_type_tv)
    @Nullable
    TextView userTypeTv;
    @BindView(R.id.id_card_tv)
    @Nullable
    TextView idCardTv;
    @BindView(R.id.user_left_ll)
    @Nullable
    LinearLayout userLeftLl;
    @BindView(R.id.user_right_ll)
    @Nullable
    LinearLayout userRightLl;
    @BindView(R.id.tel_tv)
    @Nullable
    TextView telTv;
    @BindView(R.id.gz_tv)
    @Nullable
    TextView gzTv;
    @BindView(R.id.remark_tv)
    @Nullable
    TextView remarkTv;
    @BindView(R.id.zx_lv)
    @Nullable
    ListView zxLv;
    FinalDb db;
    CommonAdapter<ArticleModel> articleAdapter;
    CommonAdapter<UserInfo> commonAdapter;
    List<UserInfo> userInfos;
    List<ArticleModel> articleModels;
    @BindView(R.id.recyclerview_category)
    @Nullable
    RecyclerView recyclerviewCategory;
    @BindView(R.id.contentLayout)
    @Nullable
    XRecyclerContentLayout contentLayout;
    StateView errorView;

    protected static final int MAX_PAGE = 10;
    @BindView(R.id.main_ll)
    @Nullable
    LinearLayout mainLl;

    @Override
    public void initData(Bundle savedInstanceState) {
        db = FinalDb.create(context);
        userInfos = new ArrayList<>();
        articleModels = new ArrayList<>();
        commonAdapter = new CommonAdapter<UserInfo>(context, userInfos, R.layout.item_team) {
            @Override
            public void convert(CommonViewHolder holder, UserInfo userInfo, int position) {
                holder.setText(R.id.textview_teamname, userInfo.getRealname());
                holder.setGildeView(R.id.imageview_team, userInfo.getIconurl());
                holder.setText(R.id.tvGoodsDescription, userInfo.getRemark());
                holder.setText(R.id.tvGoodsPrice, userInfo.getGongzi());
            }
        };
        articleAdapter = new CommonAdapter<ArticleModel>(context, articleModels, R.layout.item_article) {
            @Override
            public void convert(CommonViewHolder holder, ArticleModel articleModel, int position) {
                holder.setText(R.id.title_tv, articleModel.getTitle());
                holder.setText(R.id.content_tv, articleModel.getContent());
            }
        };
        switch (index) {
            case 0:
                List<UserInfo> list = db.findAll(UserInfo.class);
                if (list.size() > 0) {
                    UserInfo info = list.get(0);
                    Glide.with(context)
                            .load(info.getIconurl())
                            .into(userIv);
                    userNameTv.setText(info.getRealname());
                    idCardTv.setText(info.getCardnum());
                    userTypeTv.setText(info.getTypeName());
                    userLeftLl.setOnClickListener(v -> {
                    });
                    userRightLl.setOnClickListener(v -> {
                    });
                    telTv.setText("电话：" + Utils.getCache(Config.KEY_User_ID));
                    gzTv.setText("工资：" + info.getGongzi());
                    remarkTv.setText("备注：" + info.getRemark());
                }
                break;
            case 4:
                zxLv.setAdapter(articleAdapter);
                BmobQuery<ArticleModel> query = new BmobQuery<>();
                query.findObjects(new FindListener<ArticleModel>() {
                    @Override
                    public void done(List<ArticleModel> list, BmobException e) {
                        if (e == null) {
                            articleAdapter.refresh(list);
                        }
                    }
                });
                break;
            default:
                initViews();
                break;
        }
    }

    private void initViews() {
        categoryList1 = new ArrayList<>();
        categoryList1 = db.findAllByWhere(CodeModel.class, "Type='" + index + "'");
        if (categoryList1.size() > 0) {
            CodeModel model = categoryList1.get(0);
            model.setSeleted(true);
            categoryList1.remove(0);
            categoryList1.set(0, model);
        }
        recyclerviewCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryAdapter = new CategoryAdapter(context, categoryList1);
        categoryAdapter.setOnItemClickListener(position -> {
            changeSelected(position, categoryList1);
        });
        recyclerviewCategory.setAdapter(categoryAdapter);
        getP().loadData("");
//        rightLv.setAdapter(commonAdapter);
//        rightLv.setOnItemClickListener((adapterView, view, i, l) -> {
//            Utils.IntentPost(UserDetailActivity.class, listenter -> {
//                listenter.putExtra("user", userInfos.get(i));
//            });//跳转到个人详情页面
//        });
        changeSelected(0, categoryList1);
    }

    private List<CodeModel> categoryList1;
    private CategoryAdapter categoryAdapter;
    private int oldSelectedPosition = 0;

    /**
     * 控制当前item选择
     *
     * @param position 当前点击位置
     */
    private void changeSelected(int position, List<CodeModel> list) {
        categoryList1.get(oldSelectedPosition).setSeleted(false);
        categoryList1.get(position).setSeleted(true);
        if (position < 7 || (categoryList1.size() - position) < 5) {
            recyclerviewCategory.scrollToPosition(position);
        } else {
            recyclerviewCategory.scrollToPosition(position + 5);
        }
        oldSelectedPosition = position;
        categoryAdapter.notifyDataSetChanged();
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        CodeModel codeModel = new CodeModel();
        codeModel.setObjectId(categoryList1.get(position).getObjectid());
        bmobQuery.addWhereEqualTo("type", codeModel);
        bmobQuery.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> lists, BmobException e) {
                if (e == null) {
                    userInfos = lists;
                    commonAdapter.refresh(userInfos);
                }
            }
        });
    }

    public MainFragment(int index) {
        this.index = index;
    }

    public static MainFragment newInstance(int position) {
        return new MainFragment(position);
    }

    @Override
    public int getLayoutId() {
        switch (index) {
            case 0:
                return R.layout.frag_user;
            case 4:
                return R.layout.frag_zx;
            default:
                return R.layout.frag_gz;
        }
    }

    @Override
    public PBasePager newP() {
        return new PBasePager();
    }
}
