package me.xiaopan.assemblyadapter;

import android.view.ViewGroup;

public abstract class AssemblyRecyclerItemFactory<ITEM extends AssemblyRecyclerItem>{
    private int itemType;
    private Class<?> beanClass;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public boolean isAssignableFrom(Object itemObject){
        if(beanClass == null){
            beanClass = getBeanClass();
        }
        if(itemObject == null){
            return beanClass == null;
        }
        if(beanClass == null){
            return false;
        }
        Class<?> targetClass = itemObject.getClass();
        return targetClass.isAssignableFrom(getBeanClass());
    }

    public abstract Class<?> getBeanClass();

    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
