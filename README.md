# MVP Maps

[ ![Download](https://api.bintray.com/packages/benoitletondor/maven/mvp-maps/images/download.svg) ](https://bintray.com/benoitletondor/maven/mvp-maps/_latestVersion)

MVP Maps is an Android library that contains base classes for implementing Maps into your application using MVP. This library is an extend of the base [MVPCore](https://github.com/benoitletondor/mvp-core) library.

## Concept

This is a library I built essentially for my own use to display Maps using my MVP implementation.

It provides base classes for `MapPresenter` and 2 differents kind of `MapView` implementation: an `Activity` and a `Fragment`.

**If you are unfamiliar with the concept of MVP or the base MVP library, please check the [Base MVP library](https://github.com/benoitletondor/mvp-core).**

## Dependencies

This library provides implementation for views that use the [appcompat-v7 support library](https://developer.android.com/topic/libraries/support-library/features.html), and is based on [Google Play Services](https://developers.google.com/android/guides/overview) Maps and Location. It has a strong dependency on them. 

## How to use

**Add this line to your gradle file:**

```
compile 'com.benoitletondor:mvp-maps:0.1'
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
}
```

And here's how the `MapPresenter` looks like:

```java
public class MainViewPresenterImpl extends BaseMapPresenterImpl<MainView> implements MainViewPresenter
{
	/**
     * Reference of the current map shown (don't forget to null it when onStop is called !)
     */
    @Nullable
    private GoogleMap mMap;

    @Override
    public void onStop()
    {
        // IMPORTANT: Don't forget to clear any instance of GoogleMap you have here to avoid leaks.
        // It will be recreated at next view start
        mMap = null;

        super.onStop();
    }

	@Override
    public void onMapAvailable(@NonNull GoogleMap map)
    {
        // You can store the map to perform actions on it later, like adding pins
        // IMPORTANT: Don't forget to clear it when onStop is called !
        mMap = map;
    }

    @Override
    public void onMapNotAvailable()
    {
        // Show an error to the user
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

I still have to implement Junit and Espresso tests and run them with Travis, that's the reason it's 0.1 and not 1.0 yet.

## License

    Copyright 2017 Benoit LETONDOR

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
