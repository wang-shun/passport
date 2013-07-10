define("utils",[],function(){return{uuid:function(){function s4(){return Math.floor((1+Math.random())*65536).toString(16).substring(1)}return s4()+s4()+s4()+s4()+s4()+s4()+s4()+s4()},addZero:function(num,len){num=num.toString();while(num.length<len){num="0"+num}return num},parseResponse:function(data){if(typeof data=="string"){try{data=eval("("+data+")")}catch(e){data={status:-1,statusText:"服务器故障"}}}return data},addIframe:function(url,callback){var iframe=document.createElement("iframe");iframe.src=url;iframe.style.position="absolute";iframe.style.top="1px";iframe.style.left="1px";iframe.style.width="1px";iframe.style.height="1px";if(iframe.attachEvent){iframe.attachEvent("onload",function(){callback&&callback()})}else{iframe.onload=function(){callback&&callback()}}document.body.appendChild(iframe)},getScript:function(url,callback){var script=document.createElement("script");var head=document.head;script.async=true;script.src=url;script.onload=script.onreadystatechange=function(_,isAbort){if(isAbort||!script.readyState||/loaded|complete/.test(script.readyState)){script.onload=script.onreadystatechange=null;if(script.parentNode){script.parentNode.removeChild(script)}script=null;if(!isAbort){callback()}}};head.insertBefore(script,head.firstChild)},getUrlByMail:function(mail){mail=mail.split("@")[1];if(!mail){return false}var hash={"139.com":"mail.10086.cn","gmail.com":"mail.google.com","sina.com":"mail.sina.com.cn","yeah.net":"www.yeah.net","hotmail.com":"www.hotmail.com","live.com":"www.outlook.com","live.cn":"www.outlook.com","live.com.cn":"www.outlook.com","outlook.com":"www.outlook.com","yahoo.com.cn":"mail.cn.yahoo.com","yahoo.cn":"mail.cn.yahoo.com","ymail.com":"www.ymail.com","eyou.com":"www.eyou.com","188.com":"www.188.com","foxmail.com":"www.foxmail.com"};var url;if(mail in hash){url=hash[mail]}else{url="mail."+mail}return"http://"+url}}});define("common",["./utils"],function(a){return{showBannerUnderLine:function(){$(".banner ul").show();var b=$(".banner ul li.current");if(b.length){$(".banner .underline").css("left",b.position().left).css("width",b.css("width"))}},parseHeader:function(b){$("#Header .username").html(b.username);if(b.username){$("#Header .info").show()}},bindJumpEmail:function(){$("#JumpToUrl").click(function(){if($("#JumpTarget")){window.open(a.getUrlByMail($("#JumpTarget").html()))}return false})}}});define("conf",[],function(){return{client_id:"1120",redirectUrl:"/static/api/jump.htm",thirdRedirectUrl:"/static/api/tj.htm"}});(function(e){window.uuiJQuery=e;function i(m,l){for(var k in l){if(m[k]){continue}m[k]=l[k]}}var c={};function f(k){var p=k.match(/WebKit\/([\d.]+)/),m=k.match(/(Android)\s+([\d.]+)/),o=k.match(/(iPad).*OS\s([\d_]+)/),q=!o&&k.match(/(iPhone\sOS)\s([\d_]+)/),t=k.match(/(webOS|hpwOS)[\s\/]([\d.]+)/),r=t&&k.match(/TouchPad/),s=k.match(/Kindle\/([\d.]+)/),n=k.match(/Silk\/([\d._]+)/),l=k.match(/(BlackBerry).*Version\/([\d.]+)/);if(m){c.android=true,c.version=m[2]}if(q){c.ios=c.iphone=true,c.version=q[2].replace(/_/g,".")}if(o){c.ios=c.ipad=true,c.version=o[2].replace(/_/g,".")}if(t){c.webos=true,c.version=t[2]}if(r){c.touchpad=true}if(l){c.blackberry=true,c.version=l[2]}if(s){c.kindle=true,c.version=s[1]}}if(!e.os){f(navigator.userAgent)}else{c=e.os}var j;uiDict={};function b(){return{top:(document.body.scrollTop||document.documentElement.scrollTop),left:(document.body.scrollLeft||document.documentElement.scrollLeft)}}function a(l){var k=b();return{clientX:l.clientX,clientY:l.clientY,left:l.clientX+k.left,top:l.clientY+k.top}}function h(){return Math.floor(Math.random()*65536).toString(16)}var g=c&&c.version;if(g){var d=a;a=function(k){var k=k.originalEvent;if(k.touches){return d(k.touches[0])}return d(k)}}e.fn.getUUI=function(l){var k=[];var l=l||this.uiName;this.each(function(n,o){var m=e(o).data(l);if(m&&uiDict[m]){k.push(uiDict[m])}});return k};e.fn.excUUICMD=function(m,l){var k=this.getUUI(this.uiName);e.each(k,function(n,o){o.excUUICMD&&o.excUUICMD(m,l)});return this};e.UUIBase={ismobile:g,stopPropagation:function(k){k.stopPropagation&&k.stopPropagation();k.cancelBubble=true},preventDefault:function(k){if(k&&k.preventDefault){k.preventDefault()}else{window.event.returnValue=false}return false},getScroll:b,getEPos:a,guid:function(){return(h()+h()+h()+h()+h()+h()+h()+h())},getMousePos:function(){return j},empty:function(){if(document.selection&&document.selection.empty){document.selection.empty()}else{if(window.getSelection){window.getSelection().removeAllRanges()}}},offset:function(k){return e.extend({width:k.width(),height:k.height()},k.offset())},eventHash:{mousedown:g?"touchstart":"mousedown",mousemove:g?"touchmove":"mousemove",mouseover:g?"touchstart":"mouseover",mouseup:g?"touchend":"mouseup",click:"click"},baseClass:{excUUICMD:function(l,k){if(this[l]){this[l](k)}if(l=="destroy"){this._destroy(k)}},on:function(n,l,m,k){arguments[1]=this.eventName(l);this.eventList?this.eventList.push(arguments):this.eventList=[arguments];n.on.apply(n,Array.prototype.slice.call(arguments,1));return arguments},eventName:function(k){return e.UUIBase.eventHash[k]||k},off:function(l){var m=l[0],k=this;m.off.apply(m,Array.prototype.slice.call(l,1));e.each(this.eventList,function(o,n){if(l==n){k.eventList.splice(o,1)}})},_destroy:function(){if(!this.eventList){return}e.each(this.eventList,function(k,l){l[0].off.apply(l[0],Array.prototype.slice.call(l,1))})}},data:{},css:[],init:function(){if(e.UUIBase.css.length){var l=e.UUIBase.css.join("");if(l==""){return}var k=document.createElement("style");k.setAttribute("type","text/css");k.innerHTML=l;e("head").append(k);e.UUIBase.css=[];e.UUIBase.data=[]}},create:function(k,l){e[k]=l;i(e[k].prototype,e.UUIBase.baseClass);e.fn[k]=function(n){var m=n||{};this.uiName=k;this.each(function(p,q){var o=e(q).data(k);if(o){if(m.destroy){uiDict[o].excUUICMD("destroy",m);delete uiDict[o];e(q).removeData(k)}else{n&&uiDict[o].excUUICMD("update",m)}}else{if(!m.destroy){if(m.enable===undefined&&m.disable===undefined){m.enable=true}o=k+(+(new Date()));e(q).data(k,o);uiDict[o]=new e[k](e(q),m)}}});if(m.instance){return this.getUUI()}return this}}};e(function(){e(document).on(e.UUIBase.eventHash.mousemove,function(k){j=a(k)});g&&e(document).on(e.UUIBase.eventHash.mousedown,function(k){j=a(k)})})})(jQuery);define("uuibase",function(){});(function(c){var a={type:"submit",onsinglefail:function(){},onsinglesuccess:function(){},onfocus:function(){},onblur:function(){},onformfail:function(){return false},onformsuccess:function(){return false}};function b(g,e){var f=this;f._dom=g;var d=f.options=c.extend({},a);f.guid=c.UUIBase.guid();f.update(e||{});f._bindEvent()}b.prototype={update:function(d){this.options=c.extend(this.options,d)},_types:{num:function(d){return !d.length||/^\d+$/.test(d)},cellphone:function(d){return !d.length||/^1\d{10}$/.test(d)},require:function(d){return c.trim(d).length},email:function(d){return !d.length||/^(\w)+(\.\w+)*@([\w_\-])+((\.\w+)+)$/.test(d)},min:function(e,d){return !e.length||e.length>=d},max:function(e,d){return !e.length||e.length<=d},range:function(f,e,d){return !f.length||f.length<=d&&f.length>=e}},_bindEvent:function(){var e=this;var d=e._dom;this.on(d,"submit",function(){var g=c(this);var h=true;var f=g.find(":input");f.each(function(i,j){h=e._validate(c(j))&&h});return(h?e.options.onformsuccess(g):e.options.onformfail(g))});d.on("focus",":input",function(){e.options.onfocus(c(this))});d.on("blur",":input",function(){e.options.onblur(c(this))});if(e.options.type=="blur"){d.on("blur",":input",function(){return e._validate(c(this))})}},_validate:function(s){var r=s.attr("uui-type"),f=s.attr("uui-reg"),p=this;if(!r&&!f){return true}var h=[];if(f){h.push([function(i){return new RegExp(f,"g").test(i)},[]])}if(r){r=r.split(" ");for(var m=0,g=r.length;m<g;m++){var o=r[m];var k,j=[];var e=/^(\w+)\((.*)\)$/g,q;q=e.exec(o);if(q){k=p._types[q[1]];j=q[2].split(",")}else{k=p._types[o]}h.push([k,j,o])}}for(var m=0,g=h.length;m<g;m++){var k=h[m][0],j=h[m][1],d=h[m][2];j.unshift(s.val());if(!k){continue}var n=k.apply(null,j);n?p.options.onsinglesuccess(s,d):p.options.onsinglefail(s,d);if(!n){break}}return n}};b.addType=function(d,e){b.prototype._types[d]=e};c.UUIBase.create("uuiForm",b);c(c.UUIBase.init)})(jQuery);define("uuiForm",function(){});define("form",["./utils","./conf","./uuibase","./uuiForm"],function(i,h){$.uuiForm.addType("password",function(k){return k.length<=16&&k.length>=6});$.uuiForm.addType("vpasswd",function(m,n){var k=$("#"+n.slice(0,1).toUpperCase()+n.slice(1)+"Ipt");if(k&&k.length){var l=k.val();return l==m}return true});$.uuiForm.addType("nick",function(k){return/^[a-z]([a-zA-Z0-9_.]{3,15})$/.test(k)});var e={require:function(l){var k=l.parent().prev().html();return"请填写"+k.replace("：","")},email:function(){return"邮箱格式不正确"},password:function(){return"密码长度为6-16位"},cellphone:function(){return"请输入正确的手机号码"},vpasswd:function(){return"两次密码输入不一致"},range:function(k){return""},max:function(l,k){return"输入字符请少于"+k+"个字"},nick:function(k){if(k.val().length<3||k.val().length>16){return"个性帐号长度为6-16位"}return"小写字母开头的数字、字母、下划线或组合"}};var b={email:"请输入您作为帐号的邮箱名",password:"6-16位，字母(区分大小写)、数字、符号",nick:"小写字母开头的数字、字母、下划线或组合"};var f=function(l,k){if(!l.parent().parent().find("."+k).length){l.parent().parent().append('<span class="'+k+'"></span>')}};var j=function(l,k){return l.parent().parent().find("."+k)};var c=function(l){if(l.attr("data-desc")){return l.attr("data-desc")}var k=l.attr("uui-type");k=(k||"").split(" ");var m;_.forEach(k,function(n){if(n!="require"&&!m&&b[n]){m=n}});return m?(b[m]||""):""};var d=function(m,l,k){return e[l]&&e[l](m,k)||""};var g=function(l){var k=i.uuid();l.find(".token").val(k);l.find(".vpic img").attr("src","/captcha?token="+k+"&t="+ +new Date())};var a=function(k){k.find(".vpic img,.change-vpic").click(function(){k.find(".vpic img").attr("src","/captcha?token="+k.find(".token").val()+"&t="+ +new Date());return false});k.click(function(){k.find(".form-error,.form-success").hide()})};return{render:function(l,k){k=k||{};l.uuiForm({type:"blur",onfocus:function(m){m.parent().addClass("form-el-focus");j(m,"error").hide();var n=c(m);if(n&&n.length){f(m,"desc");j(m,"desc").show().html(n)}},onblur:function(m){m.parent().removeClass("form-el-focus");j(m,"desc").hide()},onsinglefail:function(o,n){var m=n.split("(")[1];n=n.split("(")[0];m=m?m.slice(0,-1).split(","):[];var p=d(o,n,m);if(p&&p.length){f(o,"error");j(o,"desc").hide();j(o,"error").show().html(p)}},onsinglesuccess:function(n,m){j(n,"error").hide()},onformsuccess:function(m){if(!k.onbeforesubmit||k.onbeforesubmit(m)){$.post(m.attr("action"),m.serialize(),function(o){o=i.parseResponse(o);if(!+o.status){m.find(".form-success").show().find("span").html(o.statusText?o.statusText:"提交成功");k.onsuccess&&k.onsuccess(m,o)}else{var n=o.statusText?o.statusText.split("|")[0]:"未知错误";m.find(".form-error").show().find("span").html(n);k.onfailure&&k.onfailure(m)}})}return false},onformfail:function(m){m.find(".desc").hide();k.onformfail&&k.onformfail();return false}});l.append('<input type="hidden" name="token" value="" class="token"/>');l.append('<input name="client_id" value="'+h.client_id+'" type="hidden"/>');l.find(".form-btn").before('<div class="form-error"><span></span></div>');l.find(".form-btn").before('<div class="form-success"><span></span></div>');g(l);a(l)},initTel:function(m){var l,p="秒后重新获取验证码",o,q=60,k=q,n;$(".tel-valid-btn").click(function(){if(n){return}$(".main-content .form form").find(".tel-valid-error").hide();var s=$('.main-content .form form input[name="'+(m?m:"username")+'"]');if(s&&s.length){var v=s.parent().find(".error");if(!$.trim(s.val()).length){s.blur();return}if(v.length&&v.css("display")!="none"){return}}n=true;var u=$(this);o=u.html();u.html(k+p);u.addClass("tel-valid-btn-disable");var r=u.attr("action")||"/mobile/sendsms";$.get(r,{mobile:s.val(),new_mobile:s.val(),client_id:h.client_id,t:+new Date()},function(w){w=i.parseResponse(w);if(+w.status){if(+w.status!=20201){$(".main-content .form form").find(".tel-valid-error").show().html(w.statusText?w.statusText:"系统错误")}t()}});function t(){u.html(o);clearInterval(l);n=false;k=q;u.removeClass("tel-valid-btn-disable")}l=setInterval(function(){if(!--k){t()}else{u.html(k+p)}},1000)})},showFormError:function(k){$(".main-content .form form").find(".form-error").show().find("span").html(k)}}});define("feedback",["./common","./form","./conf"],function(b,d,c){var a=function(){d.render($(".main-content .form form"),{onsuccess:function(f){f.parent().html($("#Target2").html())}})};var e=function(k){if(!k||!k.length){return}var j=$(".main-content .form form select")[0];for(var h=0,f=k.length;h<f;h++){var g=new Option(k[h].typeName,k[h].id,false,false);j.options[j.options.length]=g}};return{init:function(){b.showBannerUnderLine();a();var f={};try{f=$.evalJSON(server_data).data}catch(g){window.console&&console.log(g)}b.parseHeader(f);e(f.problemTypeList)}}});