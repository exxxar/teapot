<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <link rel="stylesheet" href="{{ url('/css/style.css') }}">
</head>

<body>

<div class="slider">
    <section class="slide-section-1 visible">
        <div id="teapot">
            <div class="content">
                <div class="wrap-tea">

                    <div class="back"></div>
                    <div class="water">
                        <div class="bubble "></div>
                        <div class="bubble "></div>
                        <div class="bubble "></div>
                        <div class="bubble "></div>

                    </div>
                    <div class="teapot"><img src="img/main.png"></div>
                    <div class="display">
                        <h1>100</h1>
                        <h3>Temperature, °c</h3>
                    </div>
                    <ul class="social">
                        <li><a href=""><img src="img/fb.png" alt=""></a></li>
                        <li><a href=""><img src="img/vk.png" alt=""></a></li>
                        <li><a href=""><img src="img/yt.png" alt=""></a></li>
                    </ul>
                    <a href="#" class="power active"><img src="img/onoff.png" alt=""></a>
                </div>

            </div>

        </div>
    </section>
    <section class="slide-section-2 visible hideSlide"></section>
    <section class="slide-section-3 visible hideSlide"></section>
</div>
<div class="control">
    <ul>
        <li class="active" data-index="1"><a href="#"></a></li>
        <li data-index="2"><a href="#"></a></li>
        <li data-index="3"><a href="#"></a></li>
    </ul>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script>
    (function() {

        var temp = 0;
        var press = 0;
        var wLvl = 0;
        var pState = 0;

        function TeaPot() {
            return {
                status:getStatus,
                power: setPower,
                temperature: getTemperature,
                pressure: getPressure,
                waterlvl: getWaterLvl
            };


            function getStatus() {
                $.get("{{ url('/state/get') }}", function (data) {
                    $(".power").attr({
                        "title": data
                    });
                    pState = data;
                });

                if (pState==0)
                    $(".power").removeClass("on");
                else
                    $(".power").addClass("on");

            }

            function setPower(state) {

                if (state==0)
                    $(".power").removeClass("on");
                else
                    $(".power").addClass("on");

                    $.get("{{ url('/state/set/') }}/"+state, function (data) {
                        $(".power").attr({
                            "title": data
                        });
                        pState = data;
                    });



                return pState;
            }

            function getTemperature() {
                $.get("{{ url('/temperature/get') }}", function(data) {
                    $(".display h1").html(data);
                    temp = data==undefined?0:data;
                });

                if (temp>=95)
                    setPower(0);

                console.log(temp);
                return temp;
            }

            function getPressure() {
                $.get("{{ url('/pressure/get') }}", function(data) {
                    press = data==undefined?0:data;
                });

                console.log(press);
                return press;
            }

            function getWaterLvl() {
                $.get("{{ url('/waterlvl/get') }}", function(data) {
                    $(".water").css({"height":data*23*10,"transition":"1s"});
                    wLvl = data;
                });

                return wLvl;
            }
        }

        $(document).ready(function() {

            var teaPotHandler = new TeaPot();
            teaPotHandler.waterlvl();
            console.log(teaPotHandler.temperature(1));

            $(".control ul li").on("click", function() {
                var dataIndex = $(this).attr("data-index");
                $("[class*='-section-']").addClass("hideSlide");
                $(".slide-section-" + dataIndex).removeClass("hideSlide");
                $(".control ul li").removeClass("active");
                $(this).addClass("active");
            });

            $(".power").on("click", function() {
                if ($(this).hasClass("on")) {
                    teaPotHandler.power(0);
                    $(this).removeClass("on");
                } else {
                    teaPotHandler.power(1);
                    $(this).addClass("on");
                }

            });


            setInterval(function() {
                if (teaPotHandler.temperature()>=95)
                    $(".bubble").addClass("boiling");
                else
                    $(".bubble").removeClass("boiling");
                teaPotHandler.waterlvl();
                teaPotHandler.status();
            }, 2000);
        });



    })();

</script>

</html>
