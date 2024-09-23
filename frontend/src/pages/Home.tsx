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
      background.beginFill(0x0c0c0c);
      background.drawRect(0, 0, app.screen.width, app.screen.height);
      background.endFill();
      mainContainer.addChild(background);

      const screenCenterY = app.screen.height / 2;

      const images = [
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
        "/assets/landing/firstandF.jpg",
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
        "/assets/landing/firstandF.jpg",
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
        "/assets/landing/firstandF.jpg",
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
        "/assets/landing/firstandF.jpg",
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
        "/assets/landing/firstandF.jpg",
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
        "/assets/landing/firstandF.jpg",
        "/assets/landing/avengers1.jpg",
        "/assets/landing/fastand.jpg",
      ];

      const middleIndex = Math.floor(images.length / 2);

      const loadTextures = async () => {
        const textures = await Promise.all(
          images.map((path) => Assets.load(path))
        );

        const sprites = textures.map((texture, index) => {
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
                      duration: 2,
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
          style={{ zIndex: -1 }} // z-index 추가 확인
        ></div>
      )}

      {animationFinished && (
        <div className="min-h-screen flex flex-col">
          <header className="sticky top-0 bg-black z-10">
            <Navbar />
          </header>
          <main className="w-full h-screen z-1">
            <CircleCarousel onCardClick={handleCardClick} />
          </main>
        </div>
      )}

      {backgroundVideoUrl && (
        <div
          className="absolute top-0 left-0 w-full h-full z-0" // z-index 수정
          style={{ pointerEvents: "none" }} // 화면 클릭 가능
        >
          <iframe
            src={`https://www.youtube.com/embed/${backgroundVideoUrl}?autoplay=1&mute=1&loop=1&playlist=${backgroundVideoUrl}`}
            title="YouTube video player"
            className="w-full h-full"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
        </div>
      )}
    </div>
  );
};

export default Home;
