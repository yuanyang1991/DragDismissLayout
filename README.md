# DragDissmissLayout
## 这个啥
仿微信朋友圈浏览大图下拉返回交互。
看到这个功能的时候，第一感觉就是比较人性化，简化了大屏手机关闭浏览页误触的尴尬。github上面还没搜到相关的库，就自己造了个轮子。效果如下:
![image](https://github.com/yuanyang1991/DragDissmissLayout/blob/master/wechat_image.gif)


##怎么用

### 初始化
在Activity的onCreate方法中第一行加入如下代码：
```
        DragDismissLayout pullDownLayout = new DragDismissLayout(this);
        pullDownLayout.attachTo(this);

```

### 设置参数
DragDismissLayout需要知道前一个页面ImageView的x,y坐标，以及长和宽
```
//获取坐标以及长，宽
ImageDisplayActivity.show((int)iv1.getX(),(int)iv1.getY(),dp2px(100),dp2px(200),imgs[0],MainActivity.this);


//设置数据到DragDissmissLayout中
pullDownLayout.setTargetData(x,y,width,height);

```


### 怎么实现的
1.通过onInterceptTouchEvent判断拦截触摸事件，有的浏览大图控件有自己的交互，比如PhotoView
2.通过onTouchEvent消费事件。处理上滑下滑，临界点判断逻辑。
3.其实就是上面两点。还有就是通过Activity获DecorView,然后通过DecorView获取contentView，作为DragDissmissLayout的子布局，实现自己的滑动逻辑。
4.还有就是scroller的使用
5.具体见代码