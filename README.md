# MVP Maps

[ ![Download](https://api.bintray.com/packages/benoitletondor/maven/mvp-maps/images/download.svg) ](https://bintray.com/benoitletondor/maven/mvp-maps/_latestVersion)

MVP Maps is an Android library that contains base classes for implementing Maps into your application using MVP. This library is an extend of the base [MVPCore](https://github.com/benoitletondor/mvp-core) library.

## Concept

This is a library I built essentially for my own use to display Maps using my MVP implementation.

It provides base classes for `MapPresenter` and 2 differents kind of `MapView` implementation: an `Activity` and a `Fragment`.

**If you are unfamiliar with the concept of MVP or the base MVP library, please check the [Base MVP library](https://github.com/benoitletondor/mvp-core).**

## Dependencies

This library provides implementation for views that use the [appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat), and is based on [Google Play Services](https://developers.google.com/android/guides/overview) Maps and Location. It has a strong dependency on them. 

## How to use

**Add this line to your gradle file:**

```
implementation 'com.benoitletondor:mvp-maps:0.4'
```

To use it, every `Presenter` of your app should extends `BaseMapPresenterImpl` and every view should extend either `BaseMVPMapActivity` or `BaseMVPMapFragment`.

You must pass the id of the map container to the parent view. For an Activity, it's done on the `onCreate` method where you must call `super.onCreate(savedInstanceState, R.id.id_of_your_map_container)`. This view will be used to display the map fragment inside it, a simple `FrameLayout` will do.

Here's an exemple of the implementation within an `Activity`:

```java
public class MainActivity extends BaseMVPMapActivity<MainViewPresenter, MainView> implements MainView
{
	PresenterFactory<MainViewPresenter> mPresenterFactory; // You can inject this

	@Override
	protected PresenterFactory<MainViewPresenter> getPresenterFactory()
	{
		return mPresenterFactory;
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, R.id.map_container);
		setContentView(R.layout.activity_main);
	}

	@Override
    public void onMapReady()
    {
        // Your map is available via the getMap() method and ready to be used
        // You should probably call your presenter if you have some logic here
    }

    @Override
    public void onMapUnavailable()
    {
        // Map is unavailable (check logcat for more details).
        // You should probably call your presenter if you have some logic here
    }
}
```

And here's how the `MapPresenter` looks like:

```java
public class MainViewPresenterImpl extends BaseMapPresenterImpl<MainView> implements MainViewPresenter
{
	public MainViewPresenterImpl()
    {
        super(true); // Request user location
    }

	@NonNull
	@Override
	public LocationRequest getLocationRequest()
	{
		return LocationRequest.create()
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
			.setInterval(5000)
			.setFastestInterval(1000);
	}

	@Override
	public void onUserLocationChanged(@NonNull Location location)
	{
		// The map will be automaticaly updated with the user position
		// but you can use that method to do whatever you want with the user
		// position.
	}
}
```

##### Again, if you're not sure about anything here, a good idea would be to check the [README of the base MVP library that explains a lot in details](https://github.com/benoitletondor/mvp-core).

## Sample code

A sample application is provided (see _sample_ subfolder) implementing the MVP maps library into an application. 

You'll find:

- A sample MVP Maps `Activity`: the _main_ scene
- A sample MVP Maps `Fragment` with its containing Activity: the _fragment_ scene

This sample app also shows how to use [Dagger 2](https://github.com/google/dagger) to inject your presenters into views.

## TODO

I still have to implement Junit and Espresso tests and run them with Travis, that's the reason it's 0.4 and not 1.0 yet.

## Contributors

- [Benoit Letondor](https://github.com/Benoitletondor)
- [Marc-Alexandre Caroff](https://github.com/macaroff)

## License

    Copyright 2019 Benoit LETONDOR

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
