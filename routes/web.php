<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('index');
});

//Cc
Route::get('/state/get', "teaPotCotroller@getState");
Route::get('/state/set/{state?}', "teaPotCotroller@setState");
Route::get('/temperature/get', "teaPotCotroller@getTemperature");
Route::get('/pressure/get', "teaPotCotroller@getPressure");
Route::get('/waterlvl/get', "teaPotCotroller@getWaterLvl");


