package com.zdj.sipdemo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel是google推出的一个数据处理框架，ViewModel类是被设计用来以可感知生命周期的方式存储和管理UI相关数据。
 * ViewModel中数据会一直存活即使activity configuration发生变化。另外它生来可能目的就是与Fragment在数据共享上进行配合的。
 * 使用它常与LiveData数据前台类（类似观察者模式的数据实体回调类）进行配合一起使用。
 *
 * 有这些特征：
 * 1、让数据与UI隔离：让ViewModel来获取数据加工数据并且回调给UI层，明确职责工作分离（参考MVP，MVVM这些框架结构）
 * 2、数据与生命周期绑定：ViewModel与注册的Activity的生命周期绑定，有着与Activity同步的生命周期，
 * 这样就算网络请求的数据的异步回来后Activity已经销毁也不会出现问题，因为ViewModel也会被销毁终止数据的回调。
 * 3、数据持久化：ViewModel不会因为屏幕的旋转导致Activity重新创建而重置数据。（这样避免了Activity被旋转数据丢失的问题）
 * 4、与其他Activity独立：ViewModel的数据是独立的，它跟每一个绑定的Activity都是实例一个单独的数据（即它无法跟多个Activity绑定后同步共享数据）
 * 5、天生为了配合Fragment：ViewModel可以与一个Activity与多个Fragment绑定后共享数据。（这样子我们终于可以不用把Fragment的数据暂存到Activity里了），
 * 并且可以都与他们的生命周期关联。这样Fragment短暂的生命周期将不在让我们烦恼数据的暂存问题。
 */
public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("测试系统sip api");
    }

    public LiveData<String> getText() {
        return mText;
    }
}