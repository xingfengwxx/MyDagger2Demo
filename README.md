#### Dagger2的使用

Github地址：https://github.com/google/dagger

*Dagger 英文翻译，匕首、利器！(而前面我们了解的ButterKnife，只是黄油刀，奶油刀？后面还有Hilt，刀把，刀柄？WTF...)*

注意：小项目建议不使用，太锋利了会伤到你！

##### What's Dagger2?

Dagger，一个依赖注入框架，由大名鼎鼎Square公司开发共享。

Dagger2是Google接手后推出的Dagger 2.0版本，之后将会由Goole持续更新和维护。

上面对依赖注入已经有了很清晰的了解了，为什么要使用依赖注入这里不做重复讲解。

先看Dagger1的特性 *(稍作了解即可，不是本节内容重点)*：

- Dagger1是在编译的时候实行绑定，不过也用到了反射机制。***(也就是静态+动态的方式实现)***

- Dagger1会在运行的时候去检测是否一切都正常工作，所以使用的时候会付出一些代价，偶尔会无效和调试困难。

Daager2很多方面和Dagger1类似，但也有重要区别：

- Daager2没有再用反射。图的验证、配置和预先设置都在编译时期执行。***(纯静态方式实现，也导致缺乏些灵活性)***
- 容易调试和跟踪。
- 更好的性能，Google声称提升了13%的处理性能。*(你说啥就是啥)*
- 支持代码混淆。

##### 使用Dagger2

先从依赖开始，使用了**2.30.1**最新版本，后面的使用及源码分析皆针对于这个版本：

```java
implementation 'com.google.dagger:dagger:2.30.1'
annotationProcessor 'com.google.dagger:dagger-compiler:2.30.1' // 又用到了注解处理器
```

##### 两种方式

使用Dagger实现依赖注入，有两种方式：

- 使用构造函数方式注入；

- 使用Dagger模块注入；(@Module+@Provides)

##### 构造函数注入

默认情况下官方推荐使用构造函数注入，看具体实现：

```java
public class UserRepository {

    private final UserLocalSource userLocalSource;
    private final UserRemoteSource userRemoteSource;

	// 使用@Inject注解，告诉Dagger如何创建该类(UserRepository)的实例
    // 方法中的参数类作为 UserRepository的依赖项
    @Inject
    public UserRepository(UserLocalSource userLocalSource, UserRemoteSource userRemoteSource) {
        this.userLocalSource = userLocalSource;
        this.userRemoteSource = userRemoteSource;
    }

}
```

Dagger知道如何创建UserRepository，但是不知道怎么创建它的依赖项，所以在这些依赖项中也使用注解。

```java
public class UserLocalSource {

    @Inject
    public UserLocalSource() {
    }
}

public class UserRemoteSource {

    @Inject
    public UserRemoteSource() {
    }
}
```

使用@Component创建依赖关系图，Dagger从该图中了解需要这些依赖项时从何处获取它们。具体实现方式如下：

*由于使用@Component注解，一般命名XxxComponent;*

```java
@Component
public interface ApplicationComponent {

    // 告诉Dagger, LoginActivity请求注入。(这个方法名可以随意指定，一般用inject)
    // 所以此图需满足LoginActivity注入的所有字段的依赖关系。
    void inject(LoginActivity loginActivity);
}
```

重新构建项目，Dagger会自动生成ApplicationComponent的实现类DaggerApplicationComponent。

Dagger 通过其注解处理器创建了一个依赖关系图，其中包含三个类（`UserRepository`、`UserLocalSource` 、`UserRemoteSource`）之间的关系，并且只有一个入口点：用于获取 `UserRepository` 实例。

<img src="http://ww1.sinaimg.cn/large/0073bao7gy1gm3o6yaao8j30pc07st8z.jpg" alt="image-20201224140101036" style="zoom:67%;" align="left"/>

最后我们在LoginActivity中使用@Inject注解进行注入UserRepository：

```java
/**
 * Dagger实现注入有两种方式：
 * 1. 使用构造函数注入
 * 2. 使用Dagger模块注入
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在super.onCreate方法之前调用，避免出现fragment恢复问题
        // 在 super.onCreate() 中的恢复阶段，Activity 会附加可能需要访问 Activity 绑定的 Fragment。
        DaggerApplicationComponent.create().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 打印对象hashCode
        Log.i("zee", "userRepository.hashCode()=" + userRepository.hashCode());
    }
}
```

最终运行app看到userRepository对象已经创建，并打印了hashCode：

```java
userRepository.hashCode()=121070084
```

##### Dagger模块注入

很多时候，一些类的实例化不能使用构造方法实现，比如第三方库中的某些类 (Retrofit通过builder来创建实例)。这个时候需要使用Dagger模块注入的方式来告知Dagger怎么创建该类的实例。

假设我使用Retrofit来进行网络访问，先添加依赖项：

```java
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.retrofit2:converter-scalars:2.9.0'
```

Dagger模块是一个带有@Module注解的类，在其中使用 @Provides注解定义依赖项：

*模块是一种以语义方式封装有关如何提供对象信息的方法；	*

```java
/**
 * 假设这里有一个用来创建Retrofit对象的网络模块。
 * @Module通知Dagger这是类是一个Dagger模块
 */
@Module
public class NetworkModule {

    // @Provides告知Dagger如何创建这个方法返回类型的实例化对象。
    // 注意：只要这个方法用到了参数，那这些参数就是该类型的依赖项。(同构造方法注入方式)
    @Provides
    public LoginApiService provideLoginApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(LoginApiService.class);
    }

}

/**
 * Retrofit中使用的API接口
 */
public interface LoginApiService {

    @GET("/index.html")
    Call<String> index();
}

// 修改UserRemoteSource.java
public class UserRemoteSource {

    private final LoginApiService loginApiService;

    // LoginApiService的实例化方式上面已经使用Dagger模块实现。
    @Inject
    public UserRemoteSource(LoginApiService loginApiService) {
        this.loginApiService = loginApiService;
    }

}
```

为了使 Dagger 图了解此模块，必须将其添加到 `@Component` 接口，如下所示：

```java
// modules参数告诉Dagger构建图时应该包含哪些模块
@Component(modules = NetworkModule.class)
public interface ApplicationComponent {
    ...
}
```

在LoginActivity中注入LoginViewModel：

```java
// 简单起见，这个LoginViewModel并不是Jetpack中的ViewModel，而只是充当 ViewModel 的常规类
public class LoginViewModel {

    private final UserRepository userRepository;

    // 告知Dagger如何创建LoginViewModel实例
    // UserRepository作为依赖项，上面已经加入到了Dagger图中
    @Inject
    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

// 在Activity中注入
public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginViewModel loginViewModel;

    // ...
}
```

整个Dagger依赖关系图构建完成：

<img src="http://ww1.sinaimg.cn/large/0073bao7gy1gm3oaedyc2j30lz0alq3a.jpg" alt="image-20201224144542823" style="zoom:70%;" align="left"/>

图的入口点为LoginActivity，由于 `LoginActivity` 注入了 `LoginViewModel`，因此 Dagger 构建的图知道如何提供 `LoginViewModel` 的实例，以及如何以递归方式提供其依赖项的实例。Dagger 知道如何执行此操作，因为类的构造函数上有 `@Inject` 注释。

如果去分析生成的代码，会发现生成的 `ApplicationComponent` 内，有一种 factory 类方法，可用于获取它知道如何提供的所有类的实例。在本例中，Dagger 委托给 `ApplicationComponent` 中包含的 `NetworkModule` 来获取 `LoginApiService` 的实例。

稍作完善，就可以实现加载数据：MVVM+Dagger2+ViewModel+LiveData+Repository组合；

*具体的参考项目：demo1224_dagger02。*

##### 作用域及局部单例

在有些场景下，你可能需要注入的对象必须是单例的。

*比如希望在应用的其它功能中使用 `UserRepository`，并且可能不希望在每次需要时创建新对象，因此可以将其指定为单例。这同样适用于 `LoginApiService`：创建成本可能很高，并且还希望重复使用该对象的唯一实例。*

Dagger中使用@Singleton注解的方式来限定某个类的作用域，同一作用域下生命周期一致。这样你只需要指定这个类和其关联的组件 ( Component) 处于同一作用域，就可以实现单例了。

**注意：这里的@Singleton只是一种举例，真正的做法是自定义一个作用域注解，使用@Scope注解这个注解**，如下所示：

```java
// Creates MyCustomScope
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomScope {}
```

使用的时候，在类中使用作用域注解即可限定作用域：

```java
// 使用作用域，必须先在其关联的组件上限定该作用域
// @MyCustomScope
// @MyCustomScope 和 @Singleton名字不同表示了两个作用域，但是从用途上来看是一样的。
@Singleton
@Component(modules = {NetworkModule.class})
public interface ApplicationComponent {
	...
}

@Singleton
// @MyCustomScope
// 使用ApplicationComponent相同作用域，保持生命周期一致，从而实现了单例化
// 分析源码可以知道使用了DoubleCheck
public class SingletonObject {

    @Inject
    public SingletonObject() {
    }
}
```

本文案例中，由于ApplicationComponent的生命周期又与LoginActivity一致，所以其注入内部的对象，即使使用了同一作用域限定，那也只能在同一个Activity中保持单例，所以称为局部单例。

##### 全局单例实现

使用全局单例的用途就不必多说了，很多时候用到。Dagger实现全局单例的方式也很简单，只需将ApplicationComponent放在Application中创建，并且每次使用这个ApplicationComponent去注入对象时，这些使用同一作用域的类的实例化对象，保持着和ApplicationComponent一致的生命周期，也就保持和应用生命周期一致。

如下所示，在Application创建applicationComponent：

```java
public class MyApplication extends Application {

   public ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
   ...
}
```

接着在使用注入对象的地方，使用这个applicationComponent对象来注入：

```java
public class LoginActivity extends AppCompatActivity {

    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DaggerApplicationComponent.create().inject(this);
        // 使用MyApplication中的applicationComponent
        ((MyApplication) getApplicationContext()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
        ...
    }

}
```

前面都是针对构造函数注入方式去实现的单例，对于Dagger模块注入方式小有区别：

```java
@Module
public class NetworkModule {

    @Singleton
    // @MyCustomScope
    // 在@Provides注解的方法上指定作用域即可，但是注意，一样的必须和关联组件作用域一致。
    @Provides
    public LoginApiService provideLoginApiService() {
        ...
    }

}
```

也就是使用构造函数注入时，应在类中添加作用域注解；使用 Dagger 模块时，应在 `@Provides` 方法上添加作用域注解。

##### 遵循的一些规则

- 前面提到过，指定某个类作用域，必须先指定其组件作用域，且保持相同。
- 使用作用域注解的模块也只能在带有相同作用域注解的组件中使用。

- 开发设计时，一定要有清晰的依赖关系图，不然很容易产生依赖死循环。

##### Dagger子组件

有的时候，同一类型的实例化对象在不同的场景下对生命周期的要求不同，这种情况下你可能要关联的组件也不同，因为作用域不一样。

一般情况下，存在一个最上层的Component，而其他Component都是附属于该组件下。

在Dagger中使用子组件来处理这种复杂的逻辑关系：

```java
@Subcomponent
public interface MyComponent {

    // Factory that is used to create instances of this subcomponent
    // 用来告知父Component怎么创建这个Subcomponent
    @Subcomponent.Factory
    interface Factory {
        MyComponent create();
    }

    // 一样的声明某些指定注入类的方法
    void inject(LoginActivity loginActivity);

}
```

如需告知 Dagger `LoginComponent` 是 `ApplicationComponent` 的子组件，必须通过以下方式予以指明：

1. 创建新的 Dagger 模块（例如 `SubcomponentsModule`），并将子组件的类传递给注释的 `subcomponents` 属性。

   ```java
   // The "subcomponents" attribute in the @Module annotation tells Dagger what
   // Subcomponents are children of the Component this module is included in.
   @Module(subcomponents = LoginComponent.class)
   public class SubcomponentsModule {
   }
   ```

2. 将新模块（即 `SubcomponentsModule`）添加到 `ApplicationComponent`：

   ```java
   // Including SubcomponentsModule, tell ApplicationComponent that
   // LoginComponent is its subcomponent.
   @Singleton
   @Component(modules = {NetworkModule.class, SubcomponentsModule.class})
   public interface ApplicationComponent {
       ...
   }
   ```

   请注意，`ApplicationComponent` 不再需要注入 `LoginActivity`，因为现在由 `LoginComponent` 负责注入，可以从 `ApplicationComponent` 中移除 `inject()` 方法。

3. `ApplicationComponent` 的使用者需要知道如何创建 `LoginComponent` 的实例。父组件必须在其接口中添加方法，确保使用者能够根据父组件的实例创建子组件的实例：

   ```java
   // 提供在接口中创建 LoginComponent 实例的 factory
   @Singleton
   @Component(modules = { NetworkModule.class, SubcomponentsModule.class} )
   public interface ApplicationComponent {
     // ...
     // This function exposes the LoginComponent Factory out of the graph so consumers
     // can use it to obtain new instances of LoginComponent
     LoginComponent.Factory loginComponent();
   }
   ```

##### 为子组件指定作用域

每次请求时，`LoginComponent` 必须始终提供 `LoginViewModel` 的同一实例。可以通过创建自定义注释作用域并使用该作用域为 `LoginComponent` 和 `LoginViewModel` 添加注释确保这一点。请注意，不可使用 `@Singleton` 注释，因为该注释已被父组件使用，并且使对象成为应用单例（整个应用中的唯一实例）。需要创建不同的注释作用域。

**注意**，作用域限定规则如下：

- 如果某个类型标记有作用域注解，该类型就只能由带有相同作用域注解的组件使用。
- 如果某个组件标记有作用域注解，该组件就只能提供带有该注解的类型或不带注解的类型。
- 子组件不能使用其某一父组件使用的作用域注解。组件还涉及此上下文中的子组件。

在这种情况下，可能已将此作用域命名为 `@LoginScope`，但这种做法并不理想。作用域注释的名称不应明确指明其实现目的。相反，作用域注释应根据其生命周期进行命名，因为注释可以由同级组件重复使用。因此，应将其命名为 `@ActivityScope` 而不是 `@LoginScope`。

```java
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {}

@ActivityScope
@Subcomponent
public interface LoginComponent { ... }

@ActivityScope
public class LoginViewModel { ... }
```

现在，如果有两个需要 `LoginViewModel` 的 Fragment，系统就会为它们提供同一实例。例如， `LoginUsernameFragment` 和 `LoginPasswordFragment`，他们对于LoginViewModel的使用是不同的，它们需要由 `LoginComponent` 注入：

```java
@Subcomponent
public interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        LoginComponent create();
    }

    void inject(LoginActivity loginActivity);
    void inject(LoginUsernameFragment loginUsernameFragment);
    void inject(LoginPasswordFragment loginPasswordFragment);
}
```

组件访问位于 `LoginActivity` 对象中的组件的实例。`LoginUserNameFragment` 的示例代码显示在以下代码段中：

```java
public class LoginUsernameFragment extends Fragment {

    @Inject
    LoginViewModel loginViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((LoginActivity) getActivity()).loginComponent.inject(this);
 
        ...
    }
}
```

`LoginPasswordFragment`同理。

再看下新的Dagger图：

<img src="http://ww1.sinaimg.cn/large/0073bao7gy1gm3obh46y1j30jr0e7aaq.jpg" alt="image-20201224144542823" style="zoom:70%;" align="left"/>

下面我们详细介绍该图的各个部分：

1. `NetworkModule`（以及由此产生的 `LoginApiService`）包含在 `ApplicationComponent` 中，因为你在组件中指定了它。

2. `UserRepository` 保留在 `ApplicationComponent` 中，因为其作用域限定为 `ApplicationComponent`。如果项目扩大，你会希望跨不同功能（例如注册）共享同一实例。

   由于 `UserRepository` 是 `ApplicationComponent` 的一部分，其依赖项（即 `UserLocalSource` 和 `UserRemoteSource`）也必须位于此组件中，以便能够提供 `UserRepository` 的实例。

3. `LoginViewModel` 包含在 `LoginComponent` 中，因为只有 `LoginComponent` 注入的类才需要它。`LoginViewModel` 未包含在 `ApplicationComponent` 中，因为 `ApplicationComponent` 中的任何依赖项都不需要 `LoginViewModel`。

   同样，如果你尚未将 `UserRepository` 的作用域限定为 `ApplicationComponent`，Dagger 会自动将 `UserRepository` 及其依赖项作为 `LoginComponent` 的一部分包含在内，因为这是目前使用 `UserRepository` 的唯一位置。

除了将对象作用域限定为不同的生命周期之外，**创建子组件是分别封装应用的不同部分的良好做法**。

根据应用流程构建应用以创建不同的 Dagger 子图有助于在内存和启动时间方面实现**性能和扩容性更强的应用**。

##### 构建Dagger图的最佳做法

为应用构建 Dagger图时：

- 创建组件时，应该考虑什么元素会决定该组件的生命周期。在本文示例中，应用类负责 `ApplicationComponent`，而 `LoginActivity` 负责 `LoginComponent`。
- 请仅在必要时使用作用域限定。过度使用作用域限定可能会对应用的运行时性能产生负面影响：只要组件在内存中，对象就会在内存中；获取限定作用域的对象的成本更高。当 Dagger 提供对象时，它会使用 `DoubleCheck` 锁定，而不是 factory 类型提供程序。

##### 组件化中使用Dagger

多工程组件化项目中，可以如下图这样架构，只要你对作用域和子组件了解足够深入，就能灵活使用Dagger。

<img src="http://ww1.sinaimg.cn/large/0073bao7gy1gm3ocb2ylpj30og0hu76d.jpg" alt="image-20201224191023810" style="zoom:67%;" align="left"/>

但是存在一个问题，由于功能模块无法识别 `app` 模块，比如`login`模块没法识别到`app`模块中的内容，所以它没法直接通过ApplicationCompent来获取LoginComponent。

```java
public class LoginActivity extends Activity {
  ....

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Creation of the login graph using the application graph
    loginComponent = ((MyApplication) getApplicationContext())
                                    .appComponent.loginComponent().create();

    // Make Dagger instantiate @Inject fields in LoginActivity
    loginComponent.inject(this);

    ...
  }
}
```

可以定义一个 `LoginComponentProvider` 接口，该接口在 `login` 模块中为登录流程提供 `LoginComponent`：

```java
public interface LoginComponentProvider {
  public LoginComponent provideLoginComponent();
}
```

现在，`LoginActivity` 将使用该接口，而不是上面定义的代码段：

```java
public class LoginActivity extends Activity {
  ...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
     loginComponent = ((LoginComponentProvider) getApplicationContext())
                              .provideLoginComponent();

      loginComponent.inject(this);

      ...
  }
}
```

`MyApplication` 需要实现该接口以及所需的方法：

```java
public class MyApplication extends Application implements LoginComponentProvider {
  // Reference to the application graph that is used across the whole app
  ApplicationComponent appComponent = DaggerApplicationComponent.create();

  @Override
  public LoginComponent provideLoginComponent() {
    return appComponent.loginComponent.create();
  }
}
```

这就是在多模块项目中使用 Dagger 子组件的方法。项目中包含动态功能模块时，解决方案因模块之间相互依赖的方式而异。

##### 插件化中使用Dagger



#### Dagger2原理分析

##### **构建时**

在构建时，Dagger 会检查代码，并执行以下操作：

- 构建并验证依赖关系图，确保：
  - 每个对象的依赖关系都可以得到满足，从而避免出现运行时异常。
  - 不存在任何依赖循环，从而避免出现无限循环。
- 生成在运行时用于创建实际对象及其依赖项的类。

Dagger自动生成了类似这样的代码：

![1609144711(1).jpg](http://ww1.sinaimg.cn/large/0073bao7gy1gm3nskhdbqj30f00c83ys.jpg)







