/*  반응형(적응형) 페이지 설정 스크립트 */
/* basic-N2 모바일 - 데스크톱 제어*/
(function() {
  $(function() {
    $(".basic-N2[id=\'Ekm6c5AlYx\']").each(function() {
      const $block = $(this);
      // Mobile Top
      $block.find(".btn-momenu").on("click", function() {
        $block.toggleClass("block-active");
        $block.find(".header-gnbitem").removeClass("item-active");
        $block.find(".header-sublist").removeAttr("style");
      });
      $block.find(".btn-moclose").on("click", function() {
        $block.removeClass("block-active");
      });
      // Mobile Gnb
      function handleGnbEvents() {
        $block.find(".header-gnbitem").each(function() {
          const $this = $(this);
          const $thislink = $this.find(".header-gnblink");
          const $sublist = $this.find(".header-sublist");
          $thislink.off("click");
          if (window.innerWidth < 992) {
            $thislink.on("click", function() {
              const $clickedItem = $(this).parents(".header-gnbitem");
              if (!$clickedItem.hasClass("item-active")) {
                $(".header-gnbitem").removeClass("item-active");
                $(".header-sublist").stop().slideUp(300);
              }
              $clickedItem.toggleClass("item-active");
              $sublist.stop().slideToggle(300);
            });
          } else {
            $sublist.removeAttr("style");
            $block.removeClass("block-active");
            $(".header-gnbitem").removeClass("item-active");
          }
        });
      }
      handleGnbEvents();
      $(window).on("resize", function() {
        handleGnbEvents();
      });
      // Full Gnb
      $block.find(".btn-allmenu").on("click", function() {
        $block.find(".header-fullmenu").addClass("fullmenu-active");
      });
      $block.find(".fullmenu-close").on("click", function() {
        $block.find(".header-fullmenu").removeClass("fullmenu-active");
      });
      // Full Gnb DecoLine
      $block.find(".fullmenu-gnbitem").each(function() {
        const $this = $(this);
        $this.on("mouseover", function() {
          if (window.innerWidth > 992) {
            $this.find(".fullmenu-gnblink").addClass("on");
          }
        });
        $this.on("mouseout", function() {
          if (window.innerWidth > 992) {
            $this.find(".fullmenu-gnblink").removeClass("on");
          }
        });
      });
    });
  });
})();
/* basic-N39 */
(function() {
  $(function() {
    $(".basic-N39[id=\'gIm6c30flk\']").each(function() {
      const $block = $(this);
      // Swiper
      const swiper = new Swiper(".basic-N39[id=\'gIm6c30flk\'] .contents-swiper", {
        slidesPerView: 1,
        spaceBetween: 0,
        loop: true,
        autoplay: {
          delay: 5000,
        },
        loop: true,
        pagination: {
          el: ".basic-N39[id=\'gIm6c30flk\'] .swiper-pagination",
          type: "fraction",
          clickable: true,
        },
        navigation: {
          nextEl: ".basic-N39[id=\'gIm6c30flk\'] .swiper-button-next",
          prevEl: ".basic-N39[id=\'gIm6c30flk\'] .swiper-button-prev",
        },
      });
      // Swiper Play, Pause Button
      const pauseButton = $block.find('.swiper-button-pause');
      const playButton = $block.find('.swiper-button-play');
      playButton.hide();
      pauseButton.show();
      pauseButton.on('click', function() {
        swiper.autoplay.stop();
        playButton.show();
        pauseButton.hide();
      });
      playButton.on('click', function() {
        swiper.autoplay.start();
        playButton.hide();
        pauseButton.show();
      });
    });
  });
})();
/* basic-N42 */
(function() {
  $(function() {
    $(".basic-N42[id=\'ohM6c30FTx\']").each(function() {
      const $block = $(this);
      // Swiper
      const swiper = new Swiper(".basic-N42[id=\'ohM6c30FTx\'] .contents-swiper", {
        slidesPerView: 'auto',
        spaceBetween: 0,
        loop: true,
        autoplay: {
          delay: 5000,
        },
        navigation: {
          nextEl: ".basic-N42[id=\'ohM6c30FTx\'] .swiper-button-next",
          prevEl: ".basic-N42[id=\'ohM6c30FTx\'] .swiper-button-prev",
        },
        pagination: {
          type: "progressbar",
          el: ".basic-N42[id=\'ohM6c30FTx\'] .swiper-pagination",
          clickable: true,
        },
      });
    });
  });
})();
