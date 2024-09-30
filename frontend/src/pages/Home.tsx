import React, { useEffect, useRef, useState } from "react";
import { Application, Sprite, Container, Graphics, Assets } from "pixi.js";
import gsap from "gsap";
import Navbar from "../components/common/Navbar";
import CircleCarousel from "../components/CircleCarousel";

const Home: React.FC = () => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const animationStopped = useRef(false);
  const [animationFinished, setAnimationFinished] = useState(false); // 애니메이션 종료 상태 관리
  const [showLanding, setShowLanding] = useState(false); // LandingPage 표시 여부 상태
  const [backgroundVideoUrl, setBackgroundVideoUrl] = useState<string | null>(
    null
  );

  useEffect(() => {
    if (sessionStorage.getItem("visited") === "true") {
      setShowLanding(false);
      setAnimationFinished(true);
    } else {
      sessionStorage.setItem("visited", "true");
      setShowLanding(true);
    }
  }, []);

  useEffect(() => {
    const handleBeforeUnload = () => {
      sessionStorage.removeItem("visited");
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  useEffect(() => {
    if (animationFinished) {
      // 애니메이션이 완료된 후 CircleCarousel에 페이드 인 효과 적용
      const timeline = gsap.timeline();
      timeline
        .fromTo(
          ".circle-carousel",
          { opacity: 0 },
          { opacity: 0.3, duration: 0.5, ease: "power2.out" } // 처음 0에서 0.3까지 서서히 증가
        )
        .to(".circle-carousel", {
          opacity: 0.5,
          duration: 0.5,
          ease: "power2.out",
        }) // 0.3에서 0.6까지 증가
        .to(".circle-carousel", {
          opacity: 1,
          duration: 1,
          ease: "power2.out",
        }); // 0.6에서 1까지 최종 증가
    }
  }, [animationFinished]);

  useEffect(() => {
    if (!showLanding) return;

    const app = new Application();

    const init = async () => {
      await app.init({
        autoStart: true,
        resizeTo: window,
        backgroundColor: 0x1e1e1e,
        sharedTicker: true,
        autoDensity: true,
        resolution: window.devicePixelRatio || 1,
      });

      if (pixiContainerRef.current) {
        pixiContainerRef.current.appendChild(app.view);
      }
    };

    const initializeApp = async () => {
      await init();

      const mainContainer = new Container();
      app.stage.addChild(mainContainer);

      const background = new Graphics();
      background.fill(0x0c0c0c);
      background.rect(0, 0, app.screen.width, app.screen.height);
      background.fill();
      mainContainer.addChild(background);

      const screenCenterY = app.screen.height / 2;

      const images = [
        "/assets/landing/landing1.jpg",
        "/assets/landing/landing2.jpg",
        "/assets/landing/landing3.jpg",
        "/assets/landing/landing4.jpg",
        "/assets/landing/landing5.jpg",
        "/assets/landing/landing6.jpg",
        "/assets/landing/landing7.jpg",
        "/assets/landing/flick.mp4",
        "/assets/landing/landing1.jpg",
        "/assets/landing/landing2.jpg",
        "/assets/landing/landing3.jpg",
        "/assets/landing/landing4.jpg",
        "/assets/landing/landing5.jpg",
        "/assets/landing/landing6.jpg",
        "/assets/landing/landing7.jpg",
      ];

      const middleIndex = Math.floor(images.length / 2);

      const loadTextures = async () => {
        const textures = await Promise.all(
          images.map((path) => Assets.load(path))
        );

        const sprites = textures.map((texture) => {
          const sprite = new Sprite(texture);
          sprite.anchor.set(0.5);
          sprite.x = app.screen.width / 2;
          sprite.y = app.screen.height + sprite.height;
          sprite.width = 650;
          sprite.height = 330;
          mainContainer.addChild(sprite);
          return sprite;
        });

        const middleSprite = sprites[middleIndex];
        const exactStopPosition = screenCenterY;

        const timeline = gsap.timeline({
          smoothChildTiming: true,
          defaults: {
            duration: 0.8,
            ease: "power2.out",
            force3D: true,
            transformPerspective: 1000,
          },
          onUpdate: () => {
            if (
              !animationStopped.current &&
              middleSprite.y <= exactStopPosition
            ) {
              animationStopped.current = true;
              gsap.killTweensOf("*");
              timeline.kill();

              middleSprite.y = exactStopPosition;

              const previousSprite = sprites[middleIndex - 1];
              const nextSprite = sprites[middleIndex + 1];

              if (previousSprite) {
                previousSprite.y =
                  exactStopPosition - previousSprite.height - 50;
              }

              if (nextSprite) {
                nextSprite.y = exactStopPosition + middleSprite.height + 50;
              }

              setTimeout(() => {
                gsap.to(middleSprite, {
                  duration: 0.8,
                  x: app.screen.width / 2,
                  y: app.screen.height / 2,
                  width: app.screen.width,
                  height: app.screen.height,
                  ease: "power2.inOut",
                  onComplete: () => {
                    gsap.to(middleSprite, {
                      duration: 3,
                      alpha: 0,
                      ease: "power2.inOut",
                      onComplete: () => {
                        setAnimationFinished(true);
                      },
                    });
                  },
                });

                gsap.to([previousSprite, nextSprite], {
                  duration: 0.8,
                  alpha: 0,
                  ease: "power2.inOut",
                });
              }, 500);
            }
          },
        });

        timeline.to(sprites, {
          y: -sprites[0].height,
          stagger: 0.15,
          duration: 0.7,
          onStart: () => {
            animationStopped.current = false;
            middleSprite.y = app.screen.height + middleSprite.height;
          },
          immediateRender: false,
        });
      };

      await loadTextures();
    };

    initializeApp();

    return () => {
      app.destroy(true, { children: true });
      gsap.killTweensOf("*");
    };
  }, [showLanding]);

  const handleCardClick = (videoUrl: string) => {
    setBackgroundVideoUrl(videoUrl);
  };

  return (
    <div className="relative w-full h-screen overflow-hidden">
      {showLanding && (
        <div
          ref={pixiContainerRef}
          className={`absolute top-0 left-0 w-full h-full ${
            animationFinished
              ? "opacity-0 transition-opacity duration-1000"
              : ""
          }`}
          style={{ zIndex: -1 }}
        ></div>
      )}

      {animationFinished && (
        <div className="min-h-screen flex flex-col relative z-20">
          <header className="sticky top-0 bg-black z-30">
            <Navbar />
          </header>
          <main className="w-full h-screen ">
            <div className="circle-carousel">
              <CircleCarousel onCardClick={handleCardClick} />
            </div>
          </main>
        </div>
      )}

      {backgroundVideoUrl && (
        <div className="absolute top-0 left-0 w-full h-full z-10">
          <iframe
            src={`https://www.youtube.com/embed/${backgroundVideoUrl}?autoplay=1&mute=1&loop=1&playlist=${backgroundVideoUrl}`}
            title="YouTube video player"
            className="w-full h-[1030px] overflow-hidden"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
        </div>
      )}
    </div>
  );
};

export default Home;
