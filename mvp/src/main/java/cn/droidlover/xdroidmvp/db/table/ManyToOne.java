package cn.droidlover.xdroidmvp.db.table;

/**
 * Created by LiuWeiJie on 2015/7/25 0025.
 * Email:1031066280@qq.com
 */

public class ManyToOne extends Property {
    private Class<?> manyClass;

    public ManyToOne() {
    }

    public Class<?> getManyClass() {
        return this.manyClass;
    }

    public void setManyClass(Class<?> manyClass) {
        this.manyClass = manyClass;
    }
}
