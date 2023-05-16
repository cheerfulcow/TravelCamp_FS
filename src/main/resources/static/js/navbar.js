document.querySelector('#navbarHome').innerHTML =
    '<nav class="fixed-top bg-dark py-3 justify-content-around align-items-center" data-bs-theme="dark">' +
    '   <div class="row align-items-center justify-content-center px-2">'+
    '       <div class="col-md-5 px-4 justify-content-start">'+
    '            <img src="/static/img/siteLogo.png" th:src="@{/img/siteLogo.png}  id="siteLogo">' +
    '            <a href="/index" id="logoText">TravelCamp</a>\n' +
    '       </div>'+
    '        <div class="col-md-2 navLinks">\n' +
    '            <a href="/about">О нас</a>\n' +
    '        </div>\n' +
    '        <div class="col-md-2 navLinks">\n' +
    '            <a href="/tours">Наши туры</a>\n' +
    '        </div>\n' +
    '        <div class="col-md-3 offset-md-0 navLinks">\n' +
    '           <a href="/authentication">Войти в аккаунт</a>\n' +
    '        </div>\n' +
    '    </div>'+
    '</nav>'
