[![](https://jitpack.io/v/chuross/recyclerview-adapters.svg)](https://jitpack.io/#chuross/recyclerview-adapters)

# RecyclerViewAdapters
Easy way to build `RecyclerView.Adapter`.
This adapter also has multiple ItemViewType, if needed.

![sample](https://cloud.githubusercontent.com/assets/1422031/24061304/42627b7e-0b9a-11e7-97d1-14a6cabcfd59.gif)

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
