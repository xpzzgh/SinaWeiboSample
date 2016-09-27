# SinaWeiboSample
---
> 用新浪微博的接口，实现一些简单的发微博和刷微博的操作来练习一些库的使用
---
> 关键词：RxJava、Retrofit、glide、MVP、Material Support。
---



---
## 注意：
- 为达到复习和练习知识点的作用，未使用新浪官方的网络请求，这里使用Retrofit+RxJava来实现；
- 尽可能少的使用第三方开源库；
- 由于时间短，bug还很多，但之后会慢慢修复；
- 由于新浪微博接口限制，部分功能虽然实现，但是没有数据，所以无法展现，包括转发列表没有数据显示；
- 由于新浪微博的接口限制，每日请求的次数非常有限，这里关掉了请求失败自动retry，可以自己在Constant中修改。

---
> 如有意见，希望提issue，也可以发邮件给我，xpzzwy@163.com。
> 一些界面的截图，详细的可以自己pull下来，也可以查看gif目录下的gif图。

<img src="./ScreenShot/S60927-13495513.jpg" width="240">            <img src="./ScreenShot/S60927-13500719.jpg" width="240">
<img src="./ScreenShot/S60927-13513400.jpg" width="240">            <img src="./ScreenShot/S60927-13514650.jpg" width="240">
<img src="./ScreenShot/S60927-13515448.jpg" width="240">            <img src="./ScreenShot/S60927-13532972.jpg" width="240">
<img src="./ScreenShot/S60927-13560533.jpg" width="240">            <img src="./ScreenShot/S60927-13561730.jpg" width="240">

