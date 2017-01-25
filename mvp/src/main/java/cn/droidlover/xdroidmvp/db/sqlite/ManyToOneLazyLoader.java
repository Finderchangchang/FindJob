package cn.droidlover.xdroidmvp.db.sqlite;

import cn.droidlover.xdroidmvp.db.FinalDb;

/**
 * Created by LiuWeiJie on 2015/7/25 0025.
 * Email:1031066280@qq.com
 */


public class ManyToOneLazyLoader<M, O> {
    M manyEntity;
    Class<M> manyClazz;
    Class<O> oneClazz;
    FinalDb db;
    private Object fieldValue;
    O oneEntity;
    boolean hasLoaded = false;

    public ManyToOneLazyLoader(M manyEntity, Class<M> manyClazz, Class<O> oneClazz, FinalDb db) {
        this.manyEntity = manyEntity;
        this.manyClazz = manyClazz;
        this.oneClazz = oneClazz;
        this.db = db;
    }

    public O get() {
        if(this.oneEntity == null && !this.hasLoaded) {
            this.db.loadManyToOne((DbModel)null, this.manyEntity, this.manyClazz, new Class[]{this.oneClazz});
            this.hasLoaded = true;
        }

        return this.oneEntity;
    }

    public void set(O value) {
        this.oneEntity = value;
    }

    public Object getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}

