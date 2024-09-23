import React, { useEffect, useRef, useState } from "react";
import { Application, Sprite, Container, Graphics, Assets } from "pixi.js";
import gsap from "gsap";
import Navbar from "../components/common/Navbar";
import CircleCarousel from "../components/CircleCarousel";
import { useLocation, useNavigate } from "react-router-dom";

const Home: React.FC = () => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const animationStopped = useRef(false);
  const [animationFinished, setAnimationFinished] = useState(false); // 애니메이션 종료 상태 관리
  const [showLanding, setShowLanding] = useState(false); // LandingPage 표시 여부 상태
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    // 페이지가 로드될 때마다 visited 값을 초기화하여 새로고침 여부를 다시 판단
    if (sessionStorage.getItem("visited") === "true") {
      setShowLanding(false); // 이미 방문한 경우 애니메이션 숨김
      setAnimationFinished(true); // 바로 홈 화면 표시
    } else {
      sessionStorage.setItem("visited", "true");
      setShowLanding(true); // 새로고침 시 LandingPage 표시
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
    console.log("새로고침 여부 확인:", sessionStorage.getItem("visited"));
    console.log("showLanding 상태:", showLanding);
  }, [showLanding]);

  useEffect(() => {
    if (!showLanding) return; // showLanding이 false일 때 Pixi.js 초기화 방지

    // Initialize Pixi Application
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

      // Append the view to the container
      if (pixiContainerRef.current) {
        pixiContainerRef.current.appendChild(app.view);
      }
    };

    const initializeApp = async () => {
      await init();

      // Main container setup
      const mainContainer = new Container();
      app.stage.addChild(mainContainer);

      // Background setup
      const background = new Graphics();
      background.beginFill(0x0c0c0c);
      background.drawRect(0, 0, app.screen.width, app.screen.height);
      background.endFill();
      mainContainer.addChild(background);

      // Calculate the vertical center of the screen
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

      const middleIndex = Math.floor(images.length / 2); // Middle image index

      const loadTextures = async () => {
        const textures = await Promise.all(
          images.map((path) => Assets.load(path))
        );

        // Array to keep track of all sprites for staggered animation
        const sprites = textures.map((texture, index) => {
          const sprite = new Sprite(texture);
          sprite.anchor.set(0.5);
          sprite.x = app.screen.width / 2;
          sprite.y = app.screen.height + sprite.height; // Start below the screen
          sprite.width = 650;
          sprite.height = 330;
          mainContainer.addChild(sprite);
          return sprite;
        });

        // Ensure middle sprite's center is consistently compared
        const middleSprite = sprites[middleIndex];
        const exactStopPosition = screenCenterY;

        // GSAP staggered drop-up animation
        const timeline = gsap.timeline({
          smoothChildTiming: true,
          defaults: {
            duration: 0.8,
            ease: "power2.out",
            force3D: true,
            transformPerspective: 1000,
          },
          onUpdate: () => {
            // Check if the middle sprite's center reaches the exact screen center
            if (
              !animationStopped.current &&
              middleSprite.y <= exactStopPosition
            ) {
              animationStopped.current = true;
              gsap.killTweensOf("*");
              timeline.kill();

              // Set the middle sprite to the exact stop position
              middleSprite.y = exactStopPosition;

              // Adjust adjacent images to ensure consistent positioning
              const previousSprite = sprites[middleIndex - 1];
              const nextSprite = sprites[middleIndex + 1];

              if (previousSprite) {
                previousSprite.y =
                  exactStopPosition - previousSprite.height - 50;
              }

              if (nextSprite) {
                nextSprite.y = exactStopPosition + middleSprite.height + 50;
              }

              // Wait for 1 second before zooming in
              setTimeout(() => {
                // Zoom in the middle sprite to full screen
                gsap.to(middleSprite, {
                  duration: 0.8,
                  x: app.screen.width / 2,
                  y: app.screen.height / 2,
                  width: app.screen.width,
                  height: app.screen.height,
                  ease: "power2.inOut",
                  onComplete: () => {
                    // Fade out the zoomed-in sprite
                    gsap.to(middleSprite, {
                      duration: 2, // fade out duration
                      alpha: 0,
                      ease: "power2.inOut",
                      onComplete: () => {
                        setAnimationFinished(true); // 애니메이션이 끝난 후 상태 업데이트
                      },
                    });
                  },
                });

                // Fade out previous and next sprites during the zoom-in animation
                gsap.to([previousSprite, nextSprite], {
                  duration: 0.8,
                  alpha: 0, // Gradually fade out
                  ease: "power2.inOut",
                });
              }, 500); // 1-second delay
            }
          },
        });

        // Apply the animation with stagger for evenly spaced drops
        timeline.to(sprites, {
          y: -sprites[0].height, // Move sprites up to top
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

  return (
    <div className="relative w-full h-screen overflow-hidden">
      {/* Pixi.js Container */}
      {showLanding && (
        <div
          ref={pixiContainerRef}
          className={`absolute top-0 left-0 w-full h-full ${
            animationFinished
              ? "opacity-0 transition-opacity duration-1000"
              : ""
          }`}
        ></div>
      )}

      {/* Home 요소들: 애니메이션이 끝나면 보이도록 설정 */}
      {animationFinished && (
        <div className="min-h-screen flex flex-col">
          <header className="sticky top-0 bg-black z-10">
            <Navbar />
          </header>
          <main className="w-full h-screen">
            <CircleCarousel />
          </main>
        </div>
      )}
    </div>
  );
};

export default Home;
