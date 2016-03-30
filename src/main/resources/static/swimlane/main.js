/**
 * GETでわたされた値を動的に取得したい場合は、このJSを読み込む必要あり。
 */
var param;
var test; //test 用


/**
 * 読み込み場所で使う(サンプルプログラム)
 * 
 */
window.onload = function onLoad() {
    param = GetQueryString();
    if(param !== null){
	    test = param["test"]; //この一行で値を取得できる
	    //何も入力されていなければ　no valueを設定
	    test = (test !== undefined) ? test : "no value";
		
    }
    //console.log("testの値は"+test);
}

/*
 * 値取得スクリプト
 */
function GetQueryString() {
            if (1 < document.location.search.length) {
                // 最初の1文字 (?記号) を除いた文字列を取得する
                var query = document.location.search.substring(1);
 
                // クエリの区切り記号 (&) で文字列を配列に分割する
                var parameters = query.split('&');
 
                var result = new Object();
                for (var i = 0; i < parameters.length; i++) {
                    // パラメータ名とパラメータ値に分割する
                    var element = parameters[i].split('=');
 
                    var paramName = decodeURIComponent(element[0]);
                    var paramValue = decodeURIComponent(element[1]);
 
                    // パラメータ名をキーとして連想配列に追加する
                    result[paramName] = decodeURIComponent(paramValue);
                }
                return result;
            }
            return null;
}

/**
 * CSSファイルを動的に取得
 * @param href
 * @param check
 * @returns {Boolean}
 */
function loadCss(href, check) {

	if(typeof(check) == 'undefined') check = true;

	var head = document.getElementsByTagName('head')[0];
	var link = document.createElement('link');
	link.rel = 'stylesheet';
	link.type = 'text/css';
	link.href = href;

	if(check) {
		var links = head.getElementsByTagName('link');
		for(var i = 0; i < links.length; i++) {
			if(links[i].href == link.href) return false;
		}
	}

	head.appendChild(link);
}