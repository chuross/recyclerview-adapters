[![](https://jitpack.io/v/chuross/recyclerview-adapters.svg)](https://jitpack.io/#chuross/recyclerview-adapters)

# RecyclerViewAdapters
Easy way to build `RecyclerView.Adapter`.
This adapter also has multiple ItemViewType, if needed.

![sample](https://qiita-image-store.s3.amazonaws.com/0/20629/1f78ff2a-a1d4-e231-629d-14ac01bf2fda.gif)

## Download
### Gradle
1. add JitPack repository to your project root `build.gradle`.
```
repositories {
    maven { url "https://jitpack.io" }
}
```

2. add the dependency
```
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
}
```

#### RxJava2 support
if you use RxJava2, you can use Rx support extension!
This extension provide

```
dependencies {
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters:1.x.x'
    compile 'com.github.chuross.recyclerview-adapters:recyclerview-adapters-rx:1.x.x'
}
```


