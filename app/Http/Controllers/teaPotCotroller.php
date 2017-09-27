<?php

namespace App\Http\Controllers;

use App\settings;
use Illuminate\Http\Request;

class teaPotCotroller extends Controller
{
    //

    public function getState(){
        return Settings::where('param', 'status')
            ->get()[0]["value"];
    }

    public function setState($state=0){
        $ch = curl_init();
        if ($state==0)
        {
            curl_setopt($ch, CURLOPT_URL, env("TEAPOT_URL")."/stop");
        }
        else
            curl_setopt($ch, CURLOPT_URL, env("TEAPOT_URL")."/start");

        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_exec($ch);
        curl_close($ch);
    }

    public function getTemperature(){
        return Settings::where('param', 'temperature')
            ->get()[0]["value"];
    }

    public function getPressure(){
        return Settings::where('param', 'pressure')
            ->get()[0]["value"];
    }

    public function getWaterLvl(){
        return Settings::where('param', 'waterlvl')
            ->get()[0]["value"];
    }
}
