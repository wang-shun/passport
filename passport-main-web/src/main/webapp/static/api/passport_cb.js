(function(){ 
    window.PassportSC=window.PassportSC||{};
    var scr=document.createElement('script');
    var passhref = '';
    if( location.protocol.indexOf('https') != -1 ){
        passhref = 'https://account.sogou.com/static';
    }else{
        passhref = 'http://s.account.sogou.com/u';
    }
    scr.src= passhref + '/api/sogou.js?t=2013120203';
    document.body.appendChild(scr);
})();
