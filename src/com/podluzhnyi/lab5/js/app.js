//Класс "точка"
function Point(x, y, z) {
    this.x = x;
    this.y = y;
    this.z = z;
}

//Класс для клонирования класса "точка"
function clonePoint(ptr) {
    this.x = ptr.x;
    this.y = ptr.y;
    this.z = ptr.z;
}

let canvas; //хранит текущий канвас
let ctx;//хранит контекст 2D


let pointField = [];// массив точек графика

let CenterOfCoords = new Point(750, 380, 0);//центр координат

let moveByOfCase = []; //как двигаться по массиву точек графика
let startPointOfCase = [];//начальный елемент массива точек графика
let endPointOfCase = [];//конечный елемент массива точек графика

let expandDigre = 30;//Масштабирование графика в N раз


let horizontBuffHeight = []; //буфер верхнего горизонта
let horizontBuffLow = []; //буфер нижнего горизонта
let horizontBuffLeft =[]; //буфер левостороннего горизонта
let horizontBuffRight = []; //буфер правостороннего горизонта

//поворот графика в градусах по каждой из координат
let InGradusAngles = {
    x: 0,
    y: 0,
    z: 0
};

//поворот графика в радианах. Вычисляется по ходу программы.
let angles = {
    x: 0,
    y: 0,
    z: 0
};

//вычисление поворота графика в радианах
function makeRealAngle() {
    angles.x = InGradusAngles.x / 180 * Math.PI;
    angles.y = InGradusAngles.y / 180 * Math.PI;
    angles.z = InGradusAngles.z / 180 * Math.PI;
}

//начальная инициализация горизонтов не заданными.
function fillHorizontBuffs() {
    for(let i = 0; i <= canvas.height; i += 1) {
        horizontBuffHeight[i] = 0;
        horizontBuffLow[i] = canvas.height;
        horizontBuffLeft[i] = canvas.width;
        horizontBuffRight[i] = 0;
    }

}

//функция поворота точки по оси Х
function rotatePointByX(Pnt) {
    let tmp = new clonePoint(Pnt);
    Pnt.y = Math.cos(angles.x) * tmp.y + Math.sin(angles.x) * tmp.z;
    Pnt.z = -Math.sin(angles.x) * tmp.y + Math.cos(angles.x) * tmp.z;
}

//функция поворота точки по оси Y
function rotatePointByY(Pnt) {
    let tmp = new clonePoint(Pnt);
    Pnt.x = Math.cos(angles.y) * tmp.x - Math.sin(angles.y) * tmp.z;
    Pnt.z = Math.sin(angles.y) * tmp.x + Math.cos(angles.y) * tmp.z;
}

//функция поворота точки по оси Z
function rotatePointByZ(Pnt) {
    let tmp = new clonePoint(Pnt);
    Pnt.x = Math.cos(angles.z) * tmp.x + Math.sin(angles.z) * tmp.y;
    Pnt.y = -Math.sin(angles.z) * tmp.x + Math.cos(angles.z) * tmp.y;
}

//функция перемещения графика функции в новый заданный центр координат
function MovePointToNewCenter(Pnt) {
    Pnt.x += CenterOfCoords.x;
    Pnt.y += CenterOfCoords.y;
    Pnt.z += CenterOfCoords.z;
}

//функция масштабирования графика
function expandPoint(Pnt) {

    Pnt.x *= expandDigre;
    Pnt.y *= expandDigre;
    Pnt.z *= expandDigre;

}

//функция поворота графика по всем трём заданным углам поворота
function fullRotation(Pnt) {
    rotatePointByX(Pnt);
    rotatePointByY(Pnt);
    rotatePointByZ(Pnt);
}

/*
функция, которая: изначальные точки графика
поворачивает соответственно заданному углу,
двигает график в новый центр координат,
масштабирует график,
инвертирует Y графика, т.к. у экрана точки инвертированы по
Y.
*/
function doNesRotationAndMovesAndInvertY(Pnt, show) {
    let tmp = new clonePoint(Pnt);
    tmp.y = -tmp.y;

    rotatePointByX(tmp);
    rotatePointByY(tmp);
    rotatePointByZ(tmp);

    expandPoint(tmp);

    MovePointToNewCenter(tmp);

    return new clonePoint(tmp);
}

//фукция, которая проверяет можно ли отрисовать точку.
function isPointCorrect(Pnt, isFirst, prevPnt) {
    //проверка, является ли точка числом
    let pp = Pnt.x !== NaN && Pnt.y !== NaN && Pnt.z !== NaN  ? true : false;

    //флаг корректнотси.
    let correct = false;

    let x = Math.round(Pnt.x); // координата округляется, тк используется как инедкс по массиву
    let y = Pnt.y;

    //проверка на: выше ли этот Y текущего макс. значения Y-горизонта на X координате
    if(horizontBuffHeight[x] < y) {
        correct = true;
        horizontBuffHeight[x] = y;
    }

    //проверка на: ниже ли этот Y текущего мин. значения Y-горизонта на X координате
    if(horizontBuffLow[x] > y) {

        correct = true;
        horizontBuffLow[x] = y;
    }

    y = Math.round(Pnt.y);//координата округляется, тк используется как инедкс по массиву
    x = Pnt.x;

    //проверка на: левее ли этот X текущего левого значения X-горизонта на Y координате
    if(horizontBuffLeft[y] > x) {
        horizontBuffLeft[y] = x;
        correct = true;
    }


    //проверка на: правее ли этот X текущего правого значения X-горизонта на Y координате
    if(horizontBuffRight[y] < x) {
        horizontBuffRight[y] = x;
        correct = true;
    }

    //если точка дозволена, то нужно выстроить новые горизонты.
    if(correct) {
        if(isFirst === false) { //выстраиваем новый горизонт, только если есть предыдущая точка

            let dx = (-Pnt.x + prevPnt.x) / 20; //шаг по X-координате до пред. точки
            let dy = (-Pnt.y + prevPnt.y) / 20; //шаг по Y-координате до пред. точки

            x = Pnt.x;
            y = Pnt.y;

            //цикл, пока рассматриваемая точка не совпала с предыдущей
            //выстраиваем новый горизонт, сравнивая все точки между
            //текущей и предыдущей точками, идём по прямой.
            while(Math.abs(x - prevPnt.x) > 1e-6 && Math.abs(y - prevPnt.y) > 1e-6) {
                if(horizontBuffHeight[Math.round(x)] < y) {
                    horizontBuffHeight[Math.round(x)] = y;
                }

                if(horizontBuffLow[Math.round(x)] > y) {
                    horizontBuffLow[Math.round(x)] = y;
                }

                if(horizontBuffLeft[Math.round(y)] > x) {
                    horizontBuffLeft[Math.round(y)] = x;
                }

                if(horizontBuffRight[Math.round(y)] < x) {
                    horizontBuffRight[Math.round(y)] = x;
                }

                x += dx;
                y += dy;
            }
        }
    }

    return pp && correct;//вернуть являеются ли координаты числом и допустима ли точка

}

//отрисовка точки
function drawPoint(Pnt) {
    let halfSize = 5;
    ctx.fillRect(Math.round(Pnt.x) - halfSize, Math.round(Pnt.y) - halfSize, 2 + halfSize, 2 + halfSize);

}

//отрисовка линии
function drawLines(Pnt) {
    ctx.lineTo(Pnt.x, Pnt. y);
}

//заполнения массива точек
//X и Y выбираются, а результат их функции обязательно записывается
//в Z
function fillPointField() {
    let i = 0, j = 0;

    for (let x = -5; x <= 5; x += 0.1, i += 1) {
        j = 0;
        let tmp = [];
        for(let y = -5; y <= 5; y += 0.1, j += 1) {
            tmp[j] = new Point(x, y, x*x + y*y);
        }
        pointField.push(tmp);
    }
}


//для алгоритма важно, чтобы отрисовка начиналась от точки
//"ближайшей" по Z-координате к пользователю
function whicIsCloser() {
    let z = doNesRotationAndMovesAndInvertY(pointField[0][0]).z;
    let cnt = 0;
    let a, b, tmp;

    a = 0;
    b = pointField[0].length - 1;
    tmp = doNesRotationAndMovesAndInvertY(pointField[a][b]);
    if(z < tmp.z) {
        z = tmp.z;
        cnt = 1;
    }

    a = pointField.length - 1;
    tmp = doNesRotationAndMovesAndInvertY(pointField[a][b]);
    if(z < tmp.z) {
        z = tmp.z;
        cnt = 3;
    }

    a = pointField.length - 1;
    b = 0;

    tmp = doNesRotationAndMovesAndInvertY(pointField[a][b]);
    if(z < tmp.z) {
        z = tmp.z;
        cnt = 2;
    }
    return cnt;
}


//функция отрисовки графика
function drawGrafik() {

    let Case = whicIsCloser();//хранит какая из начальноых точек граней "ближе"

    let start = startPointOfCase[Case];//начальная точка массива точек для отрисовки случая Case
    let end = endPointOfCase[Case];//конечная точка массива точек для отрисовки случая Case
    let moveBy = moveByOfCase[Case];// как двигаться по массиву точек в случае Case

    fillHorizontBuffs();//изначально считать горизонт не заданным

    //цикл обхода массива точек
    for(let i = start.x; i != end.x; i += moveBy.x) {

        ctx.beginPath();
        let prev = new clonePoint(start);
        let isFirst = true;//флаг, что точка первая в отрисовке линии

        for(let j = start.y; j != end.y; j += moveBy.y) {

            let tmp = new clonePoint(pointField[i][j]);//временно хранит точку графика
            let point = doNesRotationAndMovesAndInvertY(tmp);//хранит обработанную точку графика

            //условия выбора цвета отрисовки линии
            if(i % 4 % 3 === 0)
                ctx.strokeStyle = "red";
            else
                ctx.strokeStyle = "blue";

            //если это первая линия, то отрисовать жёлтым
            if(i === start.x) {
                ctx.strokeStyle = "yellow";

            }

            //если первая точка, то сделать начальной для отрисовки
            if(isFirst) {
                ctx.moveTo(point.x, point.y);
                prev = point;
                isFirst = false;
            }

            //проверка на отрисовку точки
            if(isPointCorrect(point, isFirst, prev) === true) {
                ctx.lineTo(point.x, point.y);
                isFirst = false;
            } else {

                ctx.stroke();
                ctx.closePath();
                ctx.beginPath();
            }
            prev = point;
        }

        ctx.stroke();
        ctx.closePath();
    }

}

//"очистка" экрана
function clearScreen() {
    let tmp = ctx.fillStyle;
    ctx.fillStyle = "black";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = tmp;
}


let isListening = false;//флаг, нужно ли прослушивать движение мыши или нет
let mouseStartX, mouseStartY;//хранят прошлое значение мыши при прошлом регистрировании

//добавляет обработку мыши для поворота графика
function addEventsListeners() {
    canvas.addEventListener('mousedown', function(event) {
        mouseStartX = event.pageX;
        mouseStartY = event.pageY;
        isListening = true;
    });

    canvas.addEventListener("mouseup", function(event) {
        isListening = false;
    });

    canvas.addEventListener("mousemove", function(event) {
        if(isListening === true) {

            InGradusAngles.x += (mouseStartY - event.pageY) / 10;
            InGradusAngles.y += (mouseStartX - event.pageX) / 10;
            mouseStartX = event.pageX;
            mouseStartY = event.pageY;
            makeRealAngle();
            clearScreen();
            drawGrafik();
        }
    });
}

//функция начальных приготовлений при старте программы
function preparetions() {
    canvas = document.getElementById("paint");
    ctx = canvas.getContext("2d");

    fillPointField();

    moveByOfCase.push(new Point(1, 1, 0));
    moveByOfCase.push(new Point(1, -1, 0));
    moveByOfCase.push(new Point(-1, 1, 0));
    moveByOfCase.push(new Point(-1, -1, 0));

    startPointOfCase.push(new Point(0, 0, 0));
    startPointOfCase.push(new Point(0, pointField[0].length - 1, 0));
    startPointOfCase.push(new Point(pointField.length - 1, 0, 0));
    startPointOfCase.push(new Point(pointField.length - 1, pointField[pointField.length - 1].length - 1, 0));

    endPointOfCase.push(new Point(pointField.length - 1, pointField[pointField.length - 1].length - 1, 0));
    endPointOfCase.push(new Point(pointField.length - 1, 0, 0));
    endPointOfCase.push(new Point(0, pointField[0].length - 1, 0));
    endPointOfCase.push(new Point(0, 0, 0));

    addEventsListeners();

    ctx.fillStyle = "white";
    clearScreen();

    makeRealAngle();
}

//начальная фукция при загрузке канваса
function Init() {

    preparetions();
    drawGrafik();
}