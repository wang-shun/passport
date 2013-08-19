var hexcase=0;var chrsz=8;var MIN_HTTS_TIMESTAMP=1293156753137;function hex_md5(a){return binl2hex(core_md5(str2binl(a),a.length*chrsz))}function core_md5(p,k){p[k>>5]|=128<<((k)%32);p[(((k+64)>>>9)<<4)+14]=k;var o=1732584193;var n=-271733879;var m=-1732584194;var l=271733878;for(var g=0;g<p.length;g+=16){var j=o;var h=n;var f=m;var e=l;o=md5_ff(o,n,m,l,p[g+0],7,-680876936);l=md5_ff(l,o,n,m,p[g+1],12,-389564586);m=md5_ff(m,l,o,n,p[g+2],17,606105819);n=md5_ff(n,m,l,o,p[g+3],22,-1044525330);o=md5_ff(o,n,m,l,p[g+4],7,-176418897);l=md5_ff(l,o,n,m,p[g+5],12,1200080426);m=md5_ff(m,l,o,n,p[g+6],17,-1473231341);n=md5_ff(n,m,l,o,p[g+7],22,-45705983);o=md5_ff(o,n,m,l,p[g+8],7,1770035416);l=md5_ff(l,o,n,m,p[g+9],12,-1958414417);m=md5_ff(m,l,o,n,p[g+10],17,-42063);n=md5_ff(n,m,l,o,p[g+11],22,-1990404162);o=md5_ff(o,n,m,l,p[g+12],7,1804603682);l=md5_ff(l,o,n,m,p[g+13],12,-40341101);m=md5_ff(m,l,o,n,p[g+14],17,-1502002290);n=md5_ff(n,m,l,o,p[g+15],22,1236535329);o=md5_gg(o,n,m,l,p[g+1],5,-165796510);l=md5_gg(l,o,n,m,p[g+6],9,-1069501632);m=md5_gg(m,l,o,n,p[g+11],14,643717713);n=md5_gg(n,m,l,o,p[g+0],20,-373897302);o=md5_gg(o,n,m,l,p[g+5],5,-701558691);l=md5_gg(l,o,n,m,p[g+10],9,38016083);m=md5_gg(m,l,o,n,p[g+15],14,-660478335);n=md5_gg(n,m,l,o,p[g+4],20,-405537848);o=md5_gg(o,n,m,l,p[g+9],5,568446438);l=md5_gg(l,o,n,m,p[g+14],9,-1019803690);m=md5_gg(m,l,o,n,p[g+3],14,-187363961);n=md5_gg(n,m,l,o,p[g+8],20,1163531501);o=md5_gg(o,n,m,l,p[g+13],5,-1444681467);l=md5_gg(l,o,n,m,p[g+2],9,-51403784);m=md5_gg(m,l,o,n,p[g+7],14,1735328473);n=md5_gg(n,m,l,o,p[g+12],20,-1926607734);o=md5_hh(o,n,m,l,p[g+5],4,-378558);l=md5_hh(l,o,n,m,p[g+8],11,-2022574463);m=md5_hh(m,l,o,n,p[g+11],16,1839030562);n=md5_hh(n,m,l,o,p[g+14],23,-35309556);o=md5_hh(o,n,m,l,p[g+1],4,-1530992060);l=md5_hh(l,o,n,m,p[g+4],11,1272893353);m=md5_hh(m,l,o,n,p[g+7],16,-155497632);n=md5_hh(n,m,l,o,p[g+10],23,-1094730640);o=md5_hh(o,n,m,l,p[g+13],4,681279174);l=md5_hh(l,o,n,m,p[g+0],11,-358537222);m=md5_hh(m,l,o,n,p[g+3],16,-722521979);n=md5_hh(n,m,l,o,p[g+6],23,76029189);o=md5_hh(o,n,m,l,p[g+9],4,-640364487);l=md5_hh(l,o,n,m,p[g+12],11,-421815835);m=md5_hh(m,l,o,n,p[g+15],16,530742520);n=md5_hh(n,m,l,o,p[g+2],23,-995338651);o=md5_ii(o,n,m,l,p[g+0],6,-198630844);l=md5_ii(l,o,n,m,p[g+7],10,1126891415);m=md5_ii(m,l,o,n,p[g+14],15,-1416354905);n=md5_ii(n,m,l,o,p[g+5],21,-57434055);o=md5_ii(o,n,m,l,p[g+12],6,1700485571);l=md5_ii(l,o,n,m,p[g+3],10,-1894986606);m=md5_ii(m,l,o,n,p[g+10],15,-1051523);n=md5_ii(n,m,l,o,p[g+1],21,-2054922799);o=md5_ii(o,n,m,l,p[g+8],6,1873313359);l=md5_ii(l,o,n,m,p[g+15],10,-30611744);m=md5_ii(m,l,o,n,p[g+6],15,-1560198380);n=md5_ii(n,m,l,o,p[g+13],21,1309151649);o=md5_ii(o,n,m,l,p[g+4],6,-145523070);l=md5_ii(l,o,n,m,p[g+11],10,-1120210379);m=md5_ii(m,l,o,n,p[g+2],15,718787259);n=md5_ii(n,m,l,o,p[g+9],21,-343485551);o=safe_add(o,j);n=safe_add(n,h);m=safe_add(m,f);l=safe_add(l,e)}return Array(o,n,m,l)}function md5_cmn(h,e,d,c,g,f){return safe_add(bit_rol(safe_add(safe_add(e,h),safe_add(c,f)),g),d)}function md5_ff(g,f,k,j,e,i,h){return md5_cmn((f&k)|((~f)&j),g,f,e,i,h)}function md5_gg(g,f,k,j,e,i,h){return md5_cmn((f&j)|(k&(~j)),g,f,e,i,h)}function md5_hh(g,f,k,j,e,i,h){return md5_cmn(f^k^j,g,f,e,i,h)}function md5_ii(g,f,k,j,e,i,h){return md5_cmn(k^(f|(~j)),g,f,e,i,h)}function safe_add(a,d){var c=(a&65535)+(d&65535);var b=(a>>16)+(d>>16)+(c>>16);return(b<<16)|(c&65535)}function bit_rol(a,b){return(a<<b)|(a>>>(32-b))}function binl2hex(c){var b=hexcase?"0123456789ABCDEF":"0123456789abcdef";var d="";for(var a=0;a<c.length*4;a++){d+=b.charAt((c[a>>2]>>((a%4)*8+4))&15)+b.charAt((c[a>>2]>>((a%4)*8))&15)}return d}function str2binl(d){var c=Array();var a=(1<<chrsz)-1;for(var b=0;b<d.length*chrsz;b+=chrsz){c[b>>5]|=(d.charCodeAt(b/chrsz)&a)<<(b%32)}return c}function b64_423(e){var d=new Array("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9","-","_");var f=new String();for(var c=0;c<e.length;c++){for(var a=0;a<64;a++){if(e.charAt(c)==d[a]){var b=a.toString(2);f+=("000000"+b).substr(b.length);break}}if(a==64){if(c==2){return f.substr(0,8)}else{return f.substr(0,16)}}}return f}function b2i(d){var a=0;var b=128;for(var c=0;c<8;c++,b=b/2){if(d.charAt(c)=="1"){a+=b}}return String.fromCharCode(a)}function b64_decodex(d){var b=new Array();var c;var a="";for(c=0;c<d.length;c+=4){a+=b64_423(d.substr(c,4))}for(c=0;c<a.length;c+=8){b+=b2i(a.substr(c,8))}return b}function utf8to16(l){var e,g,f,h,k,d,b,a,m;e=[];h=l.length;g=f=0;while(g<h){k=l.charCodeAt(g++);switch(k>>4){case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:e[f++]=l.charAt(g-1);break;case 12:case 13:d=l.charCodeAt(g++);e[f++]=String.fromCharCode(((k&31)<<6)|(d&63));break;case 14:d=l.charCodeAt(g++);b=l.charCodeAt(g++);e[f++]=String.fromCharCode(((k&15)<<12)|((d&63)<<6)|(b&63));break;case 15:switch(k&15){case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:d=l.charCodeAt(g++);b=l.charCodeAt(g++);a=l.charCodeAt(g++);m=((k&7)<<18)|((d&63)<<12)|((b&63)<<6)|(a&63)-65536;if(0<=m&&m<=1048575){e[f]=String.fromCharCode(((m>>>10)&1023)|55296,(m&1023)|56320)}else{e[f]="?"}break;case 8:case 9:case 10:case 11:g+=4;e[f]="?";break;case 12:case 13:g+=5;e[f]="?";break}}f++}return e.join("")}function getStringLen(b){var a=b.match(/[^\x00-\xff]/ig);return b.length+(a==null?0:a.length)}function getBrowserType(){var a=0;if(window.ActiveXObject){if(window.XMLHttpRequest&&!window.XDomainRequest){return 5}else{if(window.XDomainRequest){return 6}else{return 1}}}else{if(typeof(Components)=="object"){a=2}else{if(typeof(window.opera)=="object"){a=3}else{if(window.MessageEvent&&!document.getBoxObjectFor){a=7}else{if(navigator.appVersion.indexOf("Safari")>=0){a=4}}}}}return a}function checkCookieEnabled(){try{if(navigator.cookieEnabled==false){return false}}catch(a){}return true}Function.prototype.bindFunc=function(b){if(typeof(b)!="object"){return false}var a=this;return function(){return a.apply(b,arguments)}};var login_status="";var logout_status="";var renew_status="";var PassportCardList=[];var PassportSC={version:26,cvsid:"$Id: pp18030.js,v 1.78 2009/10/26 08:22:26 jiangyan Exp $",appid:9999,max_line_length:30,domain:"",cookie:false,email:"",bindDomainSelector:true,autopad:"",autoRedirectUrl:"",loginRedirectUrl:"",logoutRedirectUrl:"",selectorTitle:"��ѡ�������û��ʺ�����",registerUrl:this.loginProtocal+"://passport.sohu.com/web/signup.jsp",recoverUrl:this.loginProtocal+"://passport.sohu.com/web/recover.jsp",postru:"",emailPostfix:false,curDSindex:-1,usePost:0,successCalledFunc:false,curCardIndex:0,oElement:false,rootElement:false,dsElement:false,sElement:false,cElement:false,dsAnchor:false,emailInput:false,passwdInput:false,pcInput:false,loginMsg:false,iElement:false,isSetFocus:true,loginProtocal:"https",http_url:false,eInterval:false,maxIntervalCount:100,intervalCount:0,state:"0000",defualtRemPwd:"",isShowRemPwdMsg:0,campImg:"http://js.sohu.com/passport/images/pic007.gif",campImgAlt:"��Ӫ",campUrl:"http://blog.sohu.com/camp?from=",cardTitle:"���Ѻ���֪����",firstDomain:"",defaultApp:"",domainPool:["chinaren.com","sogou.com"],domainList:["uniqname","sohu.com","chinaren.com","sogou.com","vip.sohu.com","17173.com","focus.cn","game.sohu.com","37wanwan.com"],appList:{"1051":"news_say","1017":"pp","1019":"blog","1073":"t","1074":"tv","1000":"mail","1001":"club","1062":"bai","1005":"alumni","10050":"chinaren","1038":"crclub","1039":"group","1021":"music","1010":"say","1042":"cbbs","1028":"focus","1029":"17173","1013":"vip","1035":"rpggame","1044":"pinyin","1022":"relaxgame"},appName:{news_say:"����˵����",pp:"���",blog:"����",t:"΢��",tv:"��Ƶ",mail:"�ʼ�",club:"����",bai:"�����",alumni:"У��¼",chinaren:"ChinaRen",crclub:"CR����",group:"Ⱥ��",music:"���ֺ�",say:"˵��",cbbs:"У����̳",focus:"���㷿��","17173":"��Ϸ��̳",vip:"vip����",rpggame:"RPG��Ϸ",pinyin:"���뷨",relaxgame:"������Ϸ"},appUrl:{news_say:"http://i.sohu.com/scomment/home/all/",pp:"http://pp.sohu.com/",blog:"http://blog.sohu.com/",t:"http://t.sohu.com",tv:"http://tv.sohu.com",mail:"http://mail.sohu.com/",club:"http://club.sohu.com",bai:"http://bai.sohu.com",alumni:"http://class.chinaren.com",chinaren:"",crclub:"http://club.chinaren.com",group:"http://i.chinaren.com/group",say:"http://s.sogou.com",music:"http://mbox.sogou.com/",cbbs:"http://cbbs.chinaren.com",focus:"http://www.focus.cn","17173":"http://bbs.17173.com",vip:"http://vip.sohu.com",rpggame:"http://game.sohu.com",pinyin:"http://pinyin.sogou.com",relaxgame:"http://game.sohu.com/index2.htm"},appPool:false,bottomRow:[],recomServ:[],reverseFirstDomain:false,showEmailInputTip:true,usePostFix:true,gotohref:function(c){var b=document.createElement("a");if(getBrowserType()==1){b.setAttribute("href",c);document.body.appendChild(b);b.click()}else{window.location=c;return}},getDomain:function(){var b=document.domain.split(".");var a=b.length;if(a<=2){return document.domain}return b[a-2]+"."+b[a-1]},getPassportDomain:function(){var a="passport."+this.domain;if(this.domain==""){this.domain=this.getDomain()}if(this.domain in {"focus.cn":"","17173.com":"","37wanwan.com":"","51f.com":""}){a="pass."+this.domain}return a},addCookie:function(b,d,e){if(this.domain==""){this.domain=this.getDomain()}var c=b+"="+escape(d)+"; path=/; domain=."+this.domain+";";if(e>0){var a=new Date();a.setTime(a.getTime()+e*3600*1000);c=c+"expires="+a.toGMTString()+";"}document.cookie=c},addCookie2:function(b,e,f,d){var c=b+"="+e+"; path=/; domain=."+d+";";if(f>0){var a=new Date();a.setTime(a.getTime()+f*3600*1000);c=c+"expires="+a.toGMTString()+";"}document.cookie=c},getCookie:function(a){var e=document.cookie;var c=document.cookie.split("; ");var d=a+"=";for(var b=0;b<c.length;b++){if(c[b].indexOf(d)==0){return c[b].substr(d.length)}}return""},deleteCookie:function(a){if(this.domain==""){this.domain=this.getDomain()}var c=new Date();c.setTime(c.getTime()-100000);var b=this.getCookie(a);document.cookie=a+"="+b+"; expires="+c.toGMTString()+"; path=/; domain=."+this.domain+";"},preventEvent:function(a){a.cancelBubble=true;a.returnValue=false;if(a.preventDefault){a.preventDefault()}if(a.stopPropagation){a.stopPropagation()}},getPosition:function(b,a){var c=0;while(b){c+=b[a];b=b.offsetParent}return c},getTime:function(){var a=new Date();return a.getTime()},strip:function(a){return a.replace(/^\s+/,"").replace(/\s+$/,"")},reportMsg:function(b){var c="";switch(b){case"1":c+="������ͨ��֤�û���";break;case"2":c+="ͨ��֤�û���Ϊ�ʼ���ַ��ʽ";break;case"3":c+="�û�����׺����Ϊ"+arguments[1];break;case"4":c+="������ͨ��֤����";break;case"5":var a=this.strip(this.emailInput.value);if(a.lastIndexOf("@focus.cn")>0){c+="�û������������!��ѯ�绰:010-58511234"}else{c+="�û������������"}break;case"6":c+="��¼��ʱ�����Ժ�����";break;case"7":c+="��¼ʧ�ܣ�������";break;case"8":c+="������ϣ��˳�ʧ�ܣ��������˳�";break;case"9":c+="��¼ʧ�ܣ����Ժ�����";break;case"10":c+="����¼����Ƶ������24Сʱ������";break;case"11":c+="���������������鿴��������";break;case"12":c+="���������ϣ����Ժ�����";break;default:c+="��¼�������Ժ�����"}this.showMsg(c)},showMsg:function(a){if(!this.loginMsg){return}this.loginMsg.innerHTML=a},cookieHandle:function(){if(!this.cookie){this.parsePassportCookie()}if(this.cookie&&this.cookie.userid!=""){return this.cookie.userid}else{return""}},relationHandle:function(){if(!this.cookie){this.parsePassportCookie()}return this._parserRelation()},_parserRelation:function(){var b=this.cookie.relation;if(b!=null&&b.length>0){var a=b.split(";");for(var f=0;f<a.length;f++){var d=a[f].split(",");var e=d[2].split("#");for(var c=0;c<e.length;c++){if(PassportSC.appid==e[c]){return d[0]}}}}return""},getDisplayName:function(){var b=this.cookieHandle();var a=b.split("@");var d=a[0];var c=/^1\d{10}$/;if(c.test(d)){return d.substring(0,3)+"****"+d.substring(7)}else{return b}},parsePassportCookie:function(){var f=document.cookie.split("; ");for(var d=0;d<f.length;d++){if(f[d].indexOf("ppinf=")==0){var c=f[d].substr(6);break}if(f[d].indexOf("ppinfo=")==0){var c=f[d].substr(7);break}if(f[d].indexOf("passport=")==0){var c=f[d].substr(9);break}}if(d==f.length){this.cookie=false;return}try{var a=unescape(c).split("|");if(a[0]=="1"||a[0]=="2"){var b=utf8to16(b64_decodex(a[3]));this._parsePassportCookie(b);return}}catch(g){}},_parsePassportCookie:function(g){var m;var c;var d;this.cookie=new Object;m=0;c=g.indexOf(":",m);while(c!=-1){var b;var a;var j;b=g.substring(m,c);lenEnd_offset=g.indexOf(":",c+1);if(lenEnd_offset==-1){break}a=parseInt(g.substring(c+1,lenEnd_offset));j=g.substr(lenEnd_offset+1,a);if(g.charAt(lenEnd_offset+1+a)!="|"){break}this.cookie[b]=j;m=lenEnd_offset+2+a;c=g.indexOf(":",m)}relation_userid=this._parserRelation();if(relation_userid!=null&&relation_userid.length>0){this.cookie[b]=relation_userid}try{this.cookie.service=new Object;var i=this.cookie.service;i.mail=0;i.alumni=0;i.chinaren=0;i.blog=0;i.pp=0;i.club=0;i.crclub=0;i.group=0;i.say=0;i.music=0;i.focus=0;i["17173"]=0;i.vip=0;i.rpggame=0;i.pinyin=0;i.relaxgame=0;var h=this.cookie.serviceuse;if(h.charAt(0)==1){i.mail="sohu"}else{if(h.charAt(2)==1){i.mail="sogou"}else{if(this.cookie.userid.indexOf("@chinaren.com")>0){i.mail="chinaren"}}}if(h.charAt(1)==1){i.alumni=1}if(h.charAt(3)==1){i.blog=1}if(h.charAt(4)==1){i.pp=1}if(h.charAt(5)==1){i.club=1}if(h.charAt(7)==1){i.crclub=1}if(h.charAt(8)==1){i.group=1}if(h.charAt(10)==1){i.music=1}if(h.charAt(11)==1||this.cookie.userid.lastIndexOf("@focus.cn")>0){i.focus=1}if(h.charAt(12)==1||this.cookie.userid.indexOf("@17173.com")>0){i["17173"]=1}if(h.charAt(13)==1){i.vip=1}if(h.charAt(14)==1){i.rpggame=1}if(h.charAt(15)==1){i.pinyin=1}if(h.charAt(16)==1){i.relaxgame=1}}catch(f){}},parseAppid:function(){var d=this.appid.toString();var c=0;this.appPool=new Array();for(var b in this.appList){var a=this.appList[b];if(typeof(a)!="string"){continue}if(b==d){this.defaultApp=this.appName[a]}else{if(b=="1028"){this.appPool[c]={app:"focus",name:"����ҵ����̳",url:"http://house.focus.cn/group/yezhu.php"};c++;this.appPool[c]={app:"focus",name:"װ����̳",url:"http://home.focus.cn/group/group_forum.php"}}else{this.appPool[c]={app:a,name:this.appName[a],url:this.appUrl[a]}}c++}}},getBottomRow:function(){var a=0;var b=this.max_line_length-getStringLen(this.defaultApp);this.bottomRow[0]=new Array();this.bottomRow[1]=new Array();if(!this.cookie){return}a=this._getBottomRow(this.bottomRow[0],b,0);b=this.max_line_length;a=this._getBottomRow(this.bottomRow[1],b,a)},_getBottomRow:function(l,f,c){var a,d;var h=this.cookie.service;var g=this.appPool;var e=c;var k;for(d=0;e<g.length;e++){a=g[e]["app"];if(typeof(a)!="string"){continue}k=getStringLen(g[e]["name"]);if(f-k<0){break}if("news_say"==a||"pp"==a||"blog"==a||"t"==a||"tv"==a||"mail"==a||"club"==a){f-=(k+2);l[d]=g[e];if(a=="mail"){if(h.mail=="sohu"){l[d]["url"]=this.loginProtocal+"://mail.sohu.com?appid=0001"}else{if(h.mail=="sogou"){l[d]["url"]="http://mail.sogou.com"}else{l[d]["url"]="http://mail.chinaren.com"}}}d++;continue}else{continue}if(typeof(h[a])=="undefined"){continue}if(h[a]!=0){k=getStringLen(g[e]["name"]);if(f-k<0){break}f-=(k+2);l[d]=g[e];if(a=="mail"){if(h.mail=="sohu"){l[d]["url"]=this.loginProtocal+"://mail.sohu.com?appid=0001"}else{if(h.mail=="sogou"){l[d]["url"]=this.loginProtocal+"://mail.sogou.com"}else{l[d]["url"]=this.loginProtocal+"://mail.chinaren.com"}}}d++}else{if(g[e]["name"]=="ChinaRen"){continue}var b=this.recomServ.length;this.recomServ[b]=g[e];if(a=="mail"){this.recomServ[b]["url"]=this.loginProtocal+"://mail.chinaren.com"}}}return e},parseLastDomain:function(l){this.emailPostfix=new Array();var b="",o="";var a="",p="",h=[];var n=document.cookie.split("; ");for(var f=0;f<n.length;f++){if(n[f].indexOf("lastdomain=")==0){try{h=unescape(n[f].substr(11)).split("|");if(h.length==4){var d=h[3];if(d!=null&&d=="1"){this.loginProtocal="http"}}}catch(k){}break}}var c=0;if(h.length>=3){var m=utf8to16(b64_decodex(h[1]));var g=m.split("|");for(var f=0;f<g.length;f++){if(g[f]!=""){this.emailPostfix[c]=g[f];c++}}}if(this.firstDomain!=""){for(var f in l){if(this.firstDomain==l[f]){o=l[f];break}}if(o!=""){this.emailPostfix[c]=o;c++}}if(document.domain.indexOf("game.sohu.com")>=0){b="game.sohu.com";this.emailPostfix[c]=b;c++}this.emailPostfix[c]=this.domain;c++;for(var f in l){if(typeof(l[f])!="string"){continue}if(l[f]!=this.domain&&l[f]!=b&&l[f]!=o){this.emailPostfix[c]=l[f];c++}}},doPost:function(){for(var d=0;d<document.forms.length;d++){if(document.forms[d].name=="loginform"){break}}if(d==document.forms.length){document.location.href=this.loginProtocal+"://passport.sohu.com";return false}var a=getBrowserType();var c=screen.width;document.forms[d].action=this.loginProtocal+"://passport.sohu.com/sso/login_js.jsp?appid="+this.appid+"&ru="+this.postru+"&b="+a+"&w="+c+"&v="+this.version;document.forms[d].submit();return false},doLogin:function(){if(this.eInterval){return}if(arguments[0]){PassportCardList[index].doLogin()}login_status="";this.intervalCount=0;this.sElement.innerHTML="";this.email=this.strip(this.emailInput.value);var c=this.email;var b=this.strip(this.passwdInput.value);var a=0;if(this.pcInput.checked==true){a=1}if(c==""){this.reportMsg("1");this.emailInput.focus();return false}if(this.autopad!=""){var d=c.substr(c.lastIndexOf("@")+1);if(this.autopad.lastIndexOf(d)<0){this.reportMsg("3",this.autopad);this.emailInput.focus();this.passwdInput.value="";return false}}if(b==""){this.reportMsg("4");this.passwdInput.value="";this.passwdInput.focus();return false}if(this.usePost==1){return this.doPost()}this.drawPassportWait("���ڵ�¼�Ѻ�ͨ��֤�����Ժ�...");return this.loginHandle(c,b,a,this.sElement,this.loginFailCall.bindFunc(this),this.loginSuccessCall.bindFunc(this))},loginHandle:function(n,h,j,q,i,d){if(typeof(q)!="object"){return false}if(checkCookieEnabled()==false){i();return false}login_status="";var k=getBrowserType();var l=screen.width;if(this.domain==""){this.domain=this.getDomain()}var c=this.getTime();var f=hex_md5(h);var p=new Date().getTime();try{this.http_url="http://passport.sohu.com/sso/login.jsp?userid="+encodeURIComponent(n)+"&password="+f+"&appid="+this.appid+"&persistentcookie="+j+"&isSLogin=1&s="+c+"&b="+k+"&w="+l+"&pwdtype=1&v="+this.version+"&t="+p;if((this.loginProtocal=="https")&&(c>MIN_HTTS_TIMESTAMP)){var a="https://passport.sohu.com/sso/login.jsp?userid="+encodeURIComponent(n)+"&password="+f+"&appid="+this.appid+"&persistentcookie="+j+"&s="+c+"&b="+k+"&w="+l+"&pwdtype=1&v="+this.version+"&t="+p}else{var a=this.http_url}}catch(g){this.http_url="http://passport.sohu.com/sso/login.jsp?userid="+n+"&password="+f+"&appid="+this.appid+"&persistentcookie="+j+"&isSLogin=1&s="+c+"&b="+k+"&w="+l+"&pwdtype=1&v="+this.version+"&t="+p;if((this.loginProtocal=="https")&&(c>MIN_HTTS_TIMESTAMP)){var a="https://passport.sohu.com/sso/login.jsp?userid="+n+"&password="+f+"&appid="+this.appid+"&persistentcookie="+j+"&s="+c+"&b="+k+"&w="+l+"&pwdtype=1&v="+this.version+"&t="+p}else{var a=this.http_url}}if(this.domain!="sohu.com"){a+="&domain="+this.domain}if(this.loginProtocal=="https"){this.state="1100"}else{this.state="2200"}this.sendLog(q,"beginLogin","0");var m=document.createElement("script");m.src=a;m.id="loginele";q.appendChild(m);var o=this;this.eInterval=setInterval(function(){o.loginIntervalProc(i,d,q)},100);return false},sendLog:function(d,e,a){var c=document.createElement("script");var b=getBrowserType();c.src="https://passport.sohu.com/web/cardlog.jsp?desc="+e+"&loginProtocal="+this.loginProtocal+"&userid="+this.email+"&appid="+this.appid+"&browserType="+b+"&status="+login_status+"&count="+this.intervalCount+"&max="+this.maxIntervalCount+"&flag="+a;d.appendChild(c)},loginIntervalProc:function(c,e,f){if(login_status==""&&this.intervalCount<this.maxIntervalCount){this.intervalCount++;return}clearInterval(this.eInterval);this.eInterval=false;if(login_status==""&&this.intervalCount>=this.maxIntervalCount){var a="";if(this.state=="2200"){a="1";this.state="2210"}else{if(this.state=="1200"){this.state="1210";a="3"}}this.sendLog(f,"login timeout"+this.state,a)}if(login_status=="success"){this.addCookie2("pp_login_time",this.loginProtocal+"|"+this.email+"|"+this.appid+"|"+getBrowserType()+"|"+this.intervalCount+"|"+this.state,-1,"sohu.com");if(this.state=="1200"){this.sendLog(f,"login success","2")}}if(login_status!="success"||this.intervalCount>=this.maxIntervalCount){if(this.loginProtocal=="https"&&login_status==""){this.intervalCount=0;this.loginProtocal="http";this.state="1200";if(this.http_url.charAt(4)=="s"){this.http_url="http"+this.http_url.substr(5)}if(this.domain!="sohu.com"){this.http_url+="&domain="+this.domain}var d=document.createElement("script");d.src=this.http_url;f.appendChild(d);var b=this;this.eInterval=setInterval(function(){b.loginIntervalProc(c,e,f)},100)}else{c()}return}if(this.loginRedirectUrl==""){this.autoProcAllDomain("login",f)}else{this.addCookie("crossdomain",this.getTime(),336)}e()},loginFailCall:function(){this.sElement.innerHTML="";this.drawLoginForm();if(this.intervalCount>=this.maxIntervalCount){this.reportMsg("6");this.emailInput.focus()}else{if(login_status=="error3"||login_status=="error2"){this.reportMsg("5");this.passwdInput.focus()}else{if(login_status=="error5"){this.reportMsg("10");this.passwdInput.focus()}else{if(login_status=="error13"){window.location=this.loginProtocal+"://passport.sohu.com/web/remind_activate.jsp";return}else{if(login_status=="error11"){this.reportMsg("12");this.passwdInput.focus()}else{if(checkCookieEnabled()==false){this.reportMsg("11");this.emailInput.focus()}else{this.reportMsg("9");this.passwdInput.focus()}}}}}}},loginSuccessCall:function(){this.parsePassportCookie();if(this.cookie&&this.cookie.userid!=""){this.email="";if(this.loginRedirectUrl!=""){if(this.cookie.service["mail"]!="0"&&(this.appid=="1000"||this.appid=="1014"||this.appid=="1037")){if(this.domain.indexOf(this.cookie.service["mail"])==-1){this.drawLoginForm()}else{PassportSC.gotohref(this.loginRedirectUrl)}}else{if(document.location.href==this.loginRedirectUrl){document.location.reload()}else{PassportSC.gotohref(this.loginRedirectUrl)}}}else{this.getBottomRow();this.drawPassportCard();for(var a=0;a<PassportCardList.length;a++){if(a==this.curCardIndex){continue}PassportCardList[a].parsePassportCookie();PassportCardList[a].getBottomRow();PassportCardList[a].drawPassportCard()}}}else{this.drawLoginForm();this.reportMsg("7")}},doLogout:function(){if(this.eInterval){return}this.intervalCount=0;this.sElement.innerHTML="";if(this.usePost==1){window.location=this.loginProtocal+"://passport.sohu.com/sso/logout_js.jsp?s="+this.getTime()+"&ru="+this.postru}else{this.logoutHandle(this.sElement,this.logoutFailCall.bindFunc(this),this.logoutSuccessCall.bindFunc(this,"dd"))}},logoutHandle:function(f,b,e){if(typeof(f)!="object"){return false}logout_status="";if(this.domain==""){this.domain=this.getDomain()}var g=this.getTime();var d=this.loginProtocal+"://passport.sohu.com/sso/logout.jsp?s="+g+"&appid="+this.appid;if(this.domain!="sohu.com"){d+="&domain="+this.domain}var c=document.createElement("script");c.src=d;f.appendChild(c);var a=this;this.eInterval=setInterval(function(){a.logoutIntervalProc(b,e,f)},100)},logoutIntervalProc:function(a,d,e){if(logout_status==""&&this.intervalCount<this.maxIntervalCount){this.intervalCount++;return}clearInterval(this.eInterval);this.eInterval=false;if(logout_status==""&&this.intervalCount>=this.maxIntervalCount){a();var c=document.createElement("script");var b=getBrowserType();c.src=this.loginProtocal+"://passport.sohu.com/web/cardlog.jsp?desc=logout timeout&loginProtocal="+this.loginProtocal+"&userid="+this.email+"&appid="+this.appid+"&browserType="+b;e.appendChild(c);return}if(logout_status!="success"){a();return}if(this.logoutRedirectUrl==""){this.autoProcAllDomain("logout",e)}else{this.addCookie("crossdomain_logout",this.getTime(),336)}d()},logoutFailCall:function(){this.sElement.innerHTML="";this.reportMsg("8")},logoutSuccessCall:function(c){this.parseLastDomain(this.domainList);this.cookie=false;this.drawLoginForm();for(var a=0;a<PassportCardList.length;a++){if(a==this.curCardIndex){continue}PassportCardList[a].drawLoginForm()}try{logoutApp()}catch(b){}},renewCookie:function(f,b,e){if(typeof(f)!="object"){return false}if(this.domain==""){this.domain=this.getDomain()}var g=this.getTime();var d=this.loginProtocal+"://passport.sohu.com/sso/renew.jsp?s="+g;if(this.domain!="sohu.com"){d+="&domain="+this.domain}var c=document.createElement("script");c.src=d;f.appendChild(c);var a=this;this.eInterval=setInterval(function(){a.renewIntervalProc(b,e,f)},100);return false},renewIntervalProc:function(a,b,c){if(renew_status==""&&this.intervalCount<this.maxIntervalCount){this.intervalCount++;return}clearInterval(this.eInterval);this.eInterval=false;if(renew_status!="success"||this.intervalCount>=this.maxIntervalCount){try{a()}catch(d){}return}this.autoProcAllDomain("renew",c);try{b()}catch(d){}},autoProcAllDomain:function(d,c){var a=this.crossDomainIframeUrl(d);if(a){var b=document.createElement("iframe");b.src=a;b.style.width="0";b.style.height="0";c.appendChild(b)}},doCrossDomainCookie:function(f,e){if(typeof(f)!="object"){return}var b="crossdomain";if(e=="logout"){b="crossdomain_logout"}var c=this.getCookie(b);if(c==""||c=="0"){return}if(this.domain==""){this.domain=this.getDomain()}var a=this.crossDomainIframeUrl(e);if(a){var d=document.createElement("iframe");d.src=a;d.style.width="0";d.style.height="0";f.appendChild(d);this.deleteCookie(b)}},crossDomainUrl:function(c,b){var d=this.getTime();var a=this.loginProtocal+"://passport.sohu.com/sso/crossdomain.jsp?s="+d+"&action="+c+"&domain="+b;return a},crossDomainIframeUrl:function(b){var a=this.loginProtocal+"://"+this.getPassportDomain()+"/sso/crossdomain_all.jsp?action="+b;return a},setDomainCookie:function(f,e,d,b){login_status="";crossdomain_status="";var c=this.crossDomainUrl("login",e);if(c){newScript=document.createElement("script");newScript.src=c;f.appendChild(newScript)}var a=this;this.eInterval=setInterval(function(){a.setCookieIntervalProc(f,d,b)},100)},setCookieIntervalProc:function(c,b,a){if(crossdomain_status!=""){clearInterval(this.eInterval);this.eInterval=false;a();return}if(login_status==""&&this.intervalCount<this.maxIntervalCount){this.intervalCount++;return}clearInterval(this.eInterval);this.eInterval=false;if(login_status!="success"||this.intervalCount>=this.maxIntervalCount){a();return}b()},downDSindex:function(){if(this.dsAnchor.firstChild==null){return}var a=this.dsAnchor.firstChild.rows;var b=0;for(;b<a.length;b++){if(a[b].firstChild.idx==this.curDSindex){break}}if(b>=a.length-1){this.curDSindex=a[0].firstChild.idx}else{this.curDSindex=a[b+1].firstChild.idx}},upDSindex:function(){if(this.dsAnchor.firstChild==null){return}var a=this.dsAnchor.firstChild.rows;var c=-1;var b=0;for(;b<a.length;b++){if(a[b].firstChild.idx==this.curDSindex){break}c=a[b].firstChild.idx}if(b==a.length){this.curDSindex=a[0].firstChild.idx}else{if(c==-1){this.curDSindex=a[a.length-1].firstChild.idx}else{this.curDSindex=c}}},findDSindex:function(b){try{var a=this.dsAnchor.firstChild.rows;for(var c=0;c<a.length;c++){if(a[c].firstChild.idx==b){return a[c].firstChild}}}catch(d){}return false},clearFocus:function(b){if(typeof(b)!="number"){b=this.curDSindex}try{var a=this.findDSindex(b);a.className="";a.style.fontWeight="normal"}catch(c){}},setFocus:function(b){if(typeof(b)!="number"){b=this.curDSindex}try{var a=this.findDSindex(b);a.className="active"}catch(c){}},fillEmailSelect:function(){var w=this.emailInput.value;var r=/^[\u4e00-\u9fa5,a-zA-Z0-9-_.@]{1,100}$/;if(w==""||!r.test(w)){this.dsElement.style.display="none";return}var x="";var a="";var s=w.lastIndexOf("@");if(s<0){a=w}else{if(s==w.length-1){a=w.substr(0,s)}else{a=w.substr(0,s);x=w.substr(s+1)}}var m=this.getPosition(this.emailInput,"offsetLeft")-this.getPosition(this.cElement,"offsetLeft");if(document.all&&!document.addEventListener){m+=1}this.dsElement.style.marginLeft=m+"px";this.dsElement.style.marginTop=(this.getPosition(this.emailInput,"offsetTop")-this.getPosition(this.cElement,"offsetTop")+this.emailInput.offsetHeight)+"px";this.dsElement.style.zIndex="2000";this.dsElement.style.paddingRight="0";this.dsElement.style.paddingLeft="0";this.dsElement.style.paddingTop="0";this.dsElement.style.paddingBottom="0";this.dsElement.style.backgroundColor="white";this.dsElement.style.display="block";var l=document.createElement("TABLE");l.width="100%";l.cellSpacing=0;l.cellPadding=3;var b=document.createElement("TBODY");l.appendChild(b);var t=0;var d=false;var n=false;var y=-1;var o="",f="";var h=this.emailPostfix;var v=/^1.*$/;if(v.test(w)){if(this.autopad!=""){h=["mobile","qq.com","focus.cn",this.autopad]}else{h=["mobile","qq.com","focus.cn"]}}for(var u=0;u<h.length;u++){var g=h[u];if(typeof(g)!="string"){continue}if(x!=""){if(g.lastIndexOf(x)!=0){continue}}if(g.lastIndexOf("@")>0){tmp_pos=g.lastIndexOf("@");if(this.autopad!=""&&this.autopad.lastIndexOf(g.substring(tmp_pos+1))<0){continue}f=g.substring(0,tmp_pos);if(f.lastIndexOf(a)!=0){continue}if(f==a){o=g.substring(g.lastIndexOf("@")+1)}n=true}else{if(this.autopad!=""&&this.autopad.lastIndexOf(g)<0){continue}}if(g==o){continue}t++;if(y==-1){y=u}if(this.curDSindex==u){d=true}var c=document.createElement("TR");var k=document.createElement("TD");k.nowrap="true";k.align="left";if(g=="mobile"||g=="uniqname"){k.innerHTML=a}else{if(n==false){if(this.usePostFix){k.innerHTML=a+"@"+g}else{k.innerHTML=a}}else{if(this.usePostFix){k.innerHTML=g}else{k.innerHTML=g.substring(0,g.lastIndexOf("@"))}}}k.id="email_postfix_"+u;k.idx=u;var q=this;k.onmouseover=function(){q.clearFocus();q.curDSindex=this.idx;q.setFocus();this.style.cursor="hand"};k.onclick=function(){q.doSelect()};c.appendChild(k);b.appendChild(c);n=false}if(t>0){this.dsAnchor.innerHTML="";this.dsAnchor.appendChild(l);if(d==false){this.curDSindex=y}this.setFocus()}else{this.dsElement.style.display="none";this.curDSindex=-1}},doSelect:function(){this.dsElement.style.display="none";var a=this.findDSindex(this.curDSindex);if(a){var b=a.innerHTML;if(b){this.emailInput.value=b.replace(/&amp;/g,"&")}}if(this.emailInput.value!=""){this.passwdInput.focus()}},checkKeyDown:function(a){a=a||window.event;var b=a.keyCode||a.which||a.charCode;if(b==38||b==40){if(a.shiftKey==1){return}this.clearFocus();if(b==38){this.upDSindex()}else{if(b==40){this.downDSindex()}}this.setFocus()}},checkKeyPress:function(b){b=b||window.event;var c=b.keyCode||b.which||b.charCode;if(c==13){this.preventEvent(b)}else{if(c==38||c==40){if(b.shiftKey==1){return}this.preventEvent(b);this.clearFocus();if(c==38){this.upDSindex()}else{if(c==40){this.downDSindex()}}this.setFocus()}else{if(c==108||c==110||c==111||c==115){var a=this;setTimeout(function(){a.fillEmailSelect()},10)}}}},checkKeyUp:function(a){a=a||window.event;var b=a.keyCode||a.which||a.charCode;this.fillEmailSelect();if(b==13){this.doSelect()}if(getBrowserType()==7||getBrowserType()==4){if(b==38||b==40){if(a.shiftKey==1){return}this.clearFocus();if(b==38){this.upDSindex()}else{if(b==40){this.downDSindex()}}this.setFocus()}}},init:function(a){this.rootElement=a;this.rootElement.innerHTML='<div class="ppselecter" style="position: absolute; display: none;"><table width="100%" cellspacing="0" cellpadding="0"><tbody><tr><td style="" class="ppseltit" id="ppseltitId">'+this.selectorTitle+'</td></tr><tr><td height="2" /></tr><tr><td /></tr></tbody></table></div><div style="display: none;"></div><div class="passportc"></div>';if(this.selectorTitle==null||this.selectorTitle.length==0){this.rootElement.innerHTML='<div class="ppselecter" style="position: absolute; display: none;"><table width="100%" cellspacing="0" cellpadding="0"><tbody><tr></tr><tr><td height="0" /></tr><tr><td /></tr></tbody></table></div><div style="display: none;"></div><div class="passportc"></div>'}this.dsElement=this.rootElement.childNodes[0];this.sElement=this.rootElement.childNodes[1];this.cElement=this.rootElement.childNodes[2];this.dsAnchor=this.dsElement.firstChild.rows[2].firstChild;this.domain=this.getDomain();this.parseLastDomain(this.domainList);this.parseAppid();this.parsePassportCookie();this.getBottomRow();if(this.postru==""){this.postru=document.location.href}},_drawPassportCard:function(){},drawPassportCard:function(){this._drawPassportCard();var b=document.getElementById("ppcontid");b.onclick=this.doClickLink.bindFunc(this);this.$iElement();try{if(this.iElement!=null){this.successCalledFunc(this.iElement)}else{try{this.drawPassportInfo()}catch(a){}}}catch(a){this.drawPassportInfo()}},doClickLink:function(a){var h=window.event?window.event:a;var g=h.srcElement||h.target;var d=g.tagName.toLowerCase();var b=this.cookie.userid;var f=document.location.href;var e="";if(d=="img"){d=g.parentNode.tagName.toLowerCase();g=g.parentNode}if(d=="a"){var c=document.createElement("script");c.src=this.loginProtocal+"://passport.sohu.com/web/golog.jsp?userid="+b+"&fappid="+this.appid+"&furl="+f+"&turl="+g;this.iElement.appendChild(c)}},$iElement:function(){this.iElement=this.$getElementByClassName("listContA")},$getElementByClassName:function(b){var a=this.cElement.getElementsByTagName("div");for(var c=0;c<a.length;c++){if(a[c].className.lastIndexOf(b)==0){return a[c]}}},drawPassportWait:function(a){},drawPassportInfo:function(){},getRanServ:function(){var d=this.recomServ.length;if(d==0){return""}var b=Math.floor(d*(Math.random()));var c='<a href="'+this.recomServ[b]["url"]+'" target="_blank">'+this.recomServ[b]["name"]+"</a>";if(d==1){return c}var a=Math.floor(d*(Math.random()));while(b==a){a=Math.floor(d*(Math.random()))}c+=' | <a href="'+this.recomServ[a]["url"]+'" target="_blank">'+this.recomServ[a]["name"]+"</a>";return c},_drawLoginForm:function(){},drawLoginForm:function(){this._drawLoginForm();var a=this.cElement.getElementsByTagName("input");for(var c=0;c<a.length;c++){if(a[c].name=="email"){this.emailInput=a[c]}if(a[c].name=="password"){this.passwdInput=a[c]}if(a[c].name=="persistentcookie"){this.pcInput=a[c]}}this.loginMsg=this.$getElementByClassName("error");if(this.isShowRemPwdMsg==1){var b=this;this.pcInput.onclick=function(){if(b.pcInput.checked==false){return}var d=window.confirm("��������������ڱ���ͨ��֤�ĵ�¼״̬�����ɻ򹫹����������������á�����ȷ�ϱ��β�����");if(d==false){b.pcInput.checked=false}}}this.bindSelector();this.autoFillUserId();var b=this;if(this.emailInput.value==""){if(this.isSetFocus){setTimeout(function(){b.emailInput.focus()},50)}}else{if(this.isSetFocus&&this.emailInput.value!="ͨ��֤�ʺ�/�ֻ���"){setTimeout(function(){b.passwdInput.focus()},50)}}},autoFillUserId:function(){if(this.showEmailInputTip){this.showEmailInputTip=false;return}var b=this.getCookie("pptmpuserid");if(this.email.length>0){this.emailInput.value=this.email}else{this.emailInput.value=b}if(b.length>0){var a=this;setTimeout(function(){a.deleteCookie("pptmpuserid")},1000)}},bindSelector:function(){if(this.bindDomainSelector){this.curDSindex=-1;try{this.emailInput.addEventListener("mousedown",this.checkMousedown.bindFunc(this),false);this.emailInput.addEventListener("keypress",this.checkKeyPress.bindFunc(this),false);this.emailInput.addEventListener("keyup",this.checkKeyUp.bindFunc(this),false);this.emailInput.addEventListener("blur",this.doSelect.bindFunc(this),false)}catch(a){try{this.emailInput.attachEvent("onmousedown",this.checkMousedown.bindFunc(this));this.emailInput.attachEvent("onkeydown",this.checkKeyDown.bindFunc(this));this.emailInput.attachEvent("onkeypress",this.checkKeyPress.bindFunc(this));this.emailInput.attachEvent("onkeyup",this.checkKeyUp.bindFunc(this));this.emailInput.attachEvent("onblur",this.doSelect.bindFunc(this))}catch(a){}}}},checkMousedown:function(){if(this.emailInput.value=="ͨ��֤�ʺ�/�ֻ���"){this.emailInput.value="";this.emailInput.style.color="black";this.emailInput.focus();return}},drawPassport:function(element){if(typeof(element)!="object"){return}if(PassportCardList.length==0){PassportCardList[0]=this}if(!this.successCalledFunc){try{this.successCalledFunc=eval("drawAppInfo")}catch(e){this.successCalledFunc=this.drawPassportInfo}}this.init(element);if(this.cookie&&(this.cookie.userid!=""||this.relationHandle()!="")){if(this.autopad!=""){var userid=this.relationHandle()!=""?this.relationHandle():this.cookie.userid;var at=userid.lastIndexOf("@");if(at>0){if(this.autopad.lastIndexOf(userid.substr(at+1))<0){this.drawLoginForm();return}}}if(this.autoRedirectUrl!=""){PassportSC.gotohref(this.autoRedirectUrl)}else{this.drawPassportCard()}}else{this.drawLoginForm()}},drawPassportNew:function(a,c,e){if(typeof(a)!="object"){return}var b=new Function();b.prototype=this;var f=PassportCardList.length;var d=new b();d.successCalledFunc=e;d.appid=c;d.curCardIndex=f;d.isSetFocus=false;PassportCardList[f]=d;drawPassportNewInit(f,a);return},drawPassportJS:function(){if(!this.oElement||typeof(this.oElement)!="object"){return}var a=this.getCookie("ppinf");var c="http://sso.passport.sohu.com/mirror/"+this.getPassportDomain()+"/"+a;var b=document.createElement("script");b.src=c;ele.appendChild(b)},doCrossDomainIframe:function(b){var a=document.createElement("iframe");a.src=b;a.style.width="0";a.style.height="0";a.id="ifr_crossdomain";PassportSC.oElement.appendChild(a)}};if(typeof(PP_SETCROSSDOMAIN)=="undefined"){var ele=document.getElementsByTagName("head")[0];PassportSC.doCrossDomainCookie(ele,"login");PassportSC.doCrossDomainCookie(ele,"logout")}if(typeof encodeURIComponent=="undefined"){PassportSC.usePost=1}if(getBrowserType()==3&&(screen.height==5000||window.navigator.userAgent.lastIndexOf("Mini")>=0)){PassportSC.usePost=1};