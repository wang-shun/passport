define("ui",[],function(){return{checkbox:function(c){c=$(c);var d=c.data("target");if(!d){return}d=$("#"+d);var b="checkbox-checked";function a(f){if(f){c.prop("checked",c.prop("checked")?null:true)}var e=c.prop("checked");e?d.addClass(b):d.removeClass(b)}c.click(function(){a()});c.on("focus",function(){if(this.blur&&window.attachEvent){this.blur()}});d.click(function(){a(1)})}}});define("utils",[],function(){return{uuid:function(){function s4(){return Math.floor((1+Math.random())*65536).toString(16).substring(1)}return s4()+s4()+s4()+s4()+s4()+s4()+s4()+s4()},addZero:function(num,len){num=num.toString();while(num.length<len){num="0"+num}return num},parseResponse:function(data){if(typeof data=="string"){try{data=eval("("+data+")")}catch(e){data={status:-1,statusText:"服务器故障"}}}return data},addIframe:function(url,callback){var iframe=document.createElement("iframe");iframe.src=url;iframe.style.position="absolute";iframe.style.top="1px";iframe.style.left="1px";iframe.style.width="1px";iframe.style.height="1px";if(iframe.attachEvent){iframe.attachEvent("onload",function(){callback&&callback()})}else{iframe.onload=function(){callback&&callback()}}document.body.appendChild(iframe)},getScript:function(url,callback){var script=document.createElement("script");var head=document.head;script.async=true;script.src=url;script.onload=script.onreadystatechange=function(_,isAbort){if(isAbort||!script.readyState||/loaded|complete/.test(script.readyState)){script.onload=script.onreadystatechange=null;if(script.parentNode){script.parentNode.removeChild(script)}script=null;if(!isAbort){callback()}}};head.insertBefore(script,head.firstChild)},getUrlByMail:function(mail){mail=mail.split("@")[1];if(!mail){return false}var hash={"139.com":"mail.10086.cn","gmail.com":"mail.google.com","sina.com":"mail.sina.com.cn","yeah.net":"www.yeah.net","hotmail.com":"www.hotmail.com","live.com":"www.outlook.com","live.cn":"www.outlook.com","live.com.cn":"www.outlook.com","outlook.com":"www.outlook.com","yahoo.com.cn":"mail.cn.yahoo.com","yahoo.cn":"mail.cn.yahoo.com","ymail.com":"www.ymail.com","eyou.com":"www.eyou.com","188.com":"www.188.com","foxmail.com":"www.foxmail.com"};var url;if(mail in hash){url=hash[mail]}else{url="mail."+mail}return"http://"+url}}});define("conf",[],function(){return{client_id:"1120",redirectUrl:"/static/api/jump.htm",thirdRedirectUrl:"/static/api/tj.htm"}});(function(c,m,g,j){var e=("oninput" in m.createElement("input"));var i={"`":192,"!":49,"#":51,"$":52,"%":53,"^":54,"&":55,"*":56,"(":57,")":48};var n={"-":189,"+":187,"[":219,"]":221,"|":220,";":186,"'":222,",":188,"/":191,"-/":111,"-*":106,"--":109,"-+":107};var a={"←":37,"→":39,CTRL:17,SHIFT:16,ALT:18,COMMAND:91,"CAPS-LOCK":20,IME:229,INSERT:45,HOME:36,END:35,"PAGE-UP":33,"PAGE-DOWN":34,"NUM-LOCK":144};var b={".":190,"-.":110,"@":50,"↑":38,"↓":40,ENTER:13,SPACE:32,ESC:27,TAB:9};g.extend(b,i);g.extend(b,n);g.extend(b,a);function d(r){var p=[];for(var q in r){if("function"!==typeof r[q]&&r.hasOwnProperty(q)){p.push(r[q])}}return new RegExp("^("+p.join("|")+")$","")}var f=d(i);var o=d(n);var l=d(a);function k(p,q){if(!q||!p){return false}while(q&&q!==p){q=q.parentNode}return q===p}function h(x,v){var u=g(x),w=this;w.settings={container:".sugg",focusItemClass:"focus",hoverItemClass:"hover",constructItem:function(y){return g("<li/>").text(y)},getItems:function(y){return[]},getItemString:function(y,z,A){return g(A).text()},onItemSelected:function(y,z){return true}};w.isVisible=function(){return g(w.settings.container).is(":visible")};w.eleItems=[];g.extend(w.settings,v||{});function s(){w.current=-1}s();function r(y){var z=w.eleItems[y];if(!z){return}u.val(w.settings.getItemString.call(u,y,w.dataItems[y],z))}function q(){g(w.settings.container).empty();w.eleItems.splice(0,w.eleItems.length);w.dataItems=w.settings.getItems.call(u);if(!w.dataItems.length){return t()}for(var z=0;z<w.dataItems.length;++z){var y=w.settings.constructItem.call(u,w.dataItems[z]);if("string"===typeof y||(y&&y.constructor===String)){y=g(y)}w.eleItems.push(y);var A=(function(B){return function(C){if(w.settings.onItemSelected.call(u,B,w.dataItems[B])){r(B)}t()}})(z);y.click(A).hover(function(){g(this).addClass(w.settings.hoverItemClass)},function(){g(this).removeClass(w.settings.hoverItemClass)});g(w.settings.container).append(y)}s();g(w.settings.container).show()}function t(){g(w.settings.container).empty().hide();w.eleItems.splice(0,w.eleItems.length);s()}function p(y){for(var z=0;z<w.eleItems.length;++z){var A=w.eleItems[z];A.removeClass(w.settings.focusItemClass)}w.eleItems[y].addClass(w.settings.focusItemClass)}(function(){if(e){u.on("input",function(y){q()})}u.blur(function(y){setTimeout(t,200)});u.on("click focus",function(y){if(!w.isVisible()){q()}});u.keydown(function(z){var y=z.keyCode;if(o.test(y)||(f.test(y)&&z.shiftKey)){z.preventDefault();return false}else{if(b.TAB===y){if(w.isVisible()&&!z.shiftKey&&w.current<w.eleItems.length-1){p(++w.current);z.preventDefault();return false}else{if(w.isVisible()&&z.shiftKey&&w.current>0&&w.eleItems.length){p(--w.current);z.preventDefault();return false}}}else{if(b.SPACE===y||b.ENTER===y){if(w.isVisible()&&w.current>=0&&w.current<=w.eleItems.length-1){r(w.current);t();z.preventDefault()}return}}}});u.keyup(function(z){var y=z.keyCode;switch(true){case b.ESC===y:t();break;case b["↑"]===y:if(w.isVisible()&&w.current>0&&w.eleItems.length){p(--w.current)}return;case b["↓"]===y:if(w.isVisible()&&w.current<w.eleItems.length-1){p(++w.current)}return;case l.test(y):return;case b.TAB===y:return;default:}if(!e&&b.ENTER!==y&&b.SPACE!==y){q()}});g(m).click(function(y){if(!g(y.target).is(u)&&!k(g(w.settings.container),g(y.target))){t()}})})()}g.fn.suggest=function(p){g.each(g(this),function(q,r){new h(g(r),p)});return this}})(window,document,jQuery);define("jquery.sugg",function(){});define("index",["./ui","./utils","./conf","jquery.sugg"],function(h,i,d){var g=false;var j=function(){var k=$("#Login");k.parent().parent().addClass("login-vcode");if(g){return}g=true;k.find(".vcode img,.vcode a").click(function(){b();return false});b(k);return true};var a=function(){var k=$("#Login");k.parent().parent().removeClass("login-vcode")};var c={renren:[880,620],sina:[780,640],qq:[500,300]};var b=function(){if(g){$("#Login").find(".vcode img").attr("src","/captcha?token="+PassportSC.getToken()+"&t="+ +new Date())}};var e=function(k){$("#Login .vcode-error .text").html(k).parent().show()};var f=function(k){$("#Login .uname-error .text").html(k).parent().show()};return{init:function(){var m={};try{m=$.evalJSON(server_data).data}catch(o){window.console&&console.log(o)}h.checkbox("#RemChb");if($.cookie("fe_uname")){$("#Login .username input").val($.trim($.cookie("fe_uname")));$("#Login .username span").hide()}PassportSC.appid=d.client_id;PassportSC.redirectUrl=location.protocol+"//"+location.hostname+(location.port?(":"+location.port):"")+d.redirectUrl;$("#Login").on("submit",function(){var q=$("#Login");var p=q.find('input[name="password"]').val();if(!$.trim(q.find('input[name="username"]').val())||!p){f("请输入用户名密码");return false}if(p.length>16||p.length<6){f("用户名密码输入错误");return false}if($("#Login").parent().parent().hasClass("login-vcode")&&!$.trim(q.find('input[name="captcha"]').val())){e("请输入验证码");q.find('input[name="captcha"]').focus();return false}PassportSC.loginHandle(q.find('input[name="username"]').val(),q.find('input[name="password"]').val(),q.find('input[name="captcha"]').val(),q.find('input[name="autoLogin"]').prop("checked")?1:0,document.getElementById("logdiv"),function(s){var r=false;var t=q.find("input[name=captcha]");if(+s.needcaptcha){if(j()){r=true}$("#Login").parent().parent().addClass("login-vcode")}if(+s.status==20221){var u=t.val()?"验证码错误":"请输入验证码";e(u);!r&&b();t.focus()}else{if(+s.status==20230){f("登录异常，请1小时后再试");!r&&b()}else{f("用户名或密码错，请重新输入");!r&&b()}}},function(){$.cookie("fe_uname",$("#Login .username input").val(),{path:"/",expires:365});var r={};try{r=$.evalJSON(server_data).data}catch(s){window.console&&console.log(s)}if(r&&r.ru){location.href=r.ru}else{location.href="https://"+location.hostname}return});return false});var l=["@sogou.com","@sohu.com","@qq.com","@163.com","@126.com","@sina.com","@gmail.com","@21cn.com","@hotmail.com"];if(!String.prototype.startsWith){String.prototype.startsWith=function(r){if(!this||!r){return false}if(this.length<r.length){return false}for(var q=0,p=r.length;q<p;++q){if(this.charAt(q)!==r.charAt(q)){return false}}return true}}$("[name=username]").suggest({getItems:function(){var s=$.trim(this.val()),r=s.match(/@/g);if(s.length>1&&r&&1===r.length){var w=s.indexOf("@"),x=s.slice(w),q=s.replace(/@.*/,"");var t=[];for(var u=0,p=l.length;u<p;++u){if(l[u].startsWith(x)){t.push(q+l[u])}}return t}return[]}});$(document.body).click(function(){$("#Login .error").hide()});$("#Login .error a").click(function(){$("#Login .error").hide();return false});$(".login .third-login a").each(function(p,q){$(q).attr("href","https://account.sogou.com/connect/login?provider="+$(q).html()+"&client_id="+(m.client_id?m.client_id:d.client_id)+"&ru="+encodeURIComponent(location.href))});$("#Login .username input").change(function(){if(!$.trim($(this).val())){return}$.get("/web/login/checkNeedCaptcha",{username:$.trim($(this).val()),client_id:d.client_id,t:+new Date()},function(p){p=i.parseResponse(p);if(p.data.needCaptcha){j()}else{a()}})});var n=$("#Login .password input , #Login .username input , #Login .vcode input");var k;if(/se 2.x/i.test(navigator.userAgent)||/compatible;/i.test(navigator.userAgent)){k=setInterval(function(){n.each(function(p,q){if($(q).val().length){$(q).prev().hide()}})},100)}n.focus(function(){$(this).prev().hide();$(this).parent().find("b").show();k&&clearInterval(k)}).blur(function(){$(this).parent().find("b").hide();if(!$.trim($(this).val())){$(this).prev().show()}});n.parent().click(function(){$(this).find("input").focus()});window.onload=function(){n.each(function(p,q){if($(q).val()){$(q).prev().hide()}})};if(m.ru){$(".login a").each(function(p,q){if($(q).attr("href")=="#"){return}$(q).attr("href",$(q).attr("href")+"?ru="+encodeURIComponent(m.ru))})}if(m.client_id){$(".login a").each(function(p,q){if($(q).attr("href")=="#"){return}if($(q).attr("href").indexOf("client_id")!=-1){return}$(q).attr("href",$(q).attr("href")+($(q).attr("href").indexOf("?")==-1?"?":"&")+"client_id="+m.client_id)})}}}});