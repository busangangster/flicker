import React, { useEffect, useRef } from "react";
import { Application, Sprite, Container, Graphics, Assets } from "pixi.js";
import gsap from "gsap";

const LandingPage: React.FC = () => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const animationStopped = useRef(false);

  useEffect(() => {
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
  }, []);

  return (
    <div
      ref={pixiContainerRef}
      className="relative w-full h-screen overflow-hidden"
    ></div>
  );
};

export default LandingPage;
