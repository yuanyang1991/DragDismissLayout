# DragDissmissLayout
## 这是啥

仿微信朋友圈浏览大图下拉返回交互。

看到这个功能的时候，第一感觉就是比较人性化，简化了大屏手机关闭浏览页的操作，防止大屏手机上面的误操作。github上面还没搜到相关的库，就自己造了个轮子。效果如下:
![image](https://github.com/yuanyang1991/DragDissmissLayout/blob/master/wechat_image.gif)

## 使用以及介绍

### 添加依赖
1. 在项目级gradle文件中添加 maven { url "https://jitpack.io" }
2. 在app级gradle文件添加  compile 'com.github.yuanyang1991:DragDissmissLayout:1.0.0'
3. 点击 sync


### 初始化
在Activity的onCreate方法中第一行加入如下代码：
```
        DragDismissLayout pullDownLayout = new DragDismissLayout(this);
        pullDownLayout.attachTo(this);

```

### 设置参数
DragDismissLayout需要知道前一个页面ImageView的x,y坐标，以及长和宽
```
//获取目标View的绝对坐标以及长宽
    public static void show(View view,String imageUrl ,Context context){
        Location location = LocationUtil.getRawLocation(view);
        show(location.x,location.y,view.getMeasuredWidth(),view.getMeasuredHeight(),imageUrl,context);
    }

    public static void show(int x, int y, int width, int height, String imageUrl,Context context){
        Intent intent = new Intent(context,ImageDisplayActivity.class);
        intent.putExtra("x",x);
        intent.putExtra("url",imageUrl);
        intent.putExtra("y",y);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        context.startActivity(intent);
    }


//设置数据到DragDissmissLayout中
pullDownLayout.setTargetData(x,y,width,height);

```


### 怎么实现的
1. 通过onInterceptTouchEvent判断拦截触摸事件，有的浏览大图控件有自己的交互，比如PhotoView

2. 通过onTouchEvent消费事件。处理上滑下滑，临界点判断逻辑。

3. 获取View在屏幕上的绝对坐标

4. 滑动处理，Scroller，offsetTopAndBottom...

5. 具体见代码

### 缺点
1 还没实现View随着拖动大小同步改变，但是基本不影响使用
