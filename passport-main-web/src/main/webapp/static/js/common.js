/*
 * common module script
 * @author zhengxin
*/
 



define(function(){


    return{
        showBannerUnderLine: function(){
            var currentBanner = $('.banner ul li.current');
            if( currentBanner.length ){
                $('.banner .underline').css('left' , currentBanner.position().left)
                    .css('width' , currentBanner.css('width'));
            }
        },
        parseHeader: function(data){
            $('#Header .username').html(data.username);
            if( data.username ){
                $('#Header .logout').show().prev().show();
                $('#Header .logout a').click(function(){
                    $.getScript($(this).attr('href') , function(){
                        if( window['logout_status'] == 'success' ){
                            location.reload();
                        }else{
                            alert('系统错误');
                        }
                    });
                    return false;
                });
            }
        }
    };
});
