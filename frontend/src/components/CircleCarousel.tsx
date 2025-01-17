import React, { useEffect, useRef } from "react";
import { Application, Sprite, Assets, Texture, Graphics } from "pixi.js";
import { CircleCarouselProps, ExtendedSprite } from "../type";
import gsap from "gsap";

const CircleCarousel: React.FC<CircleCarouselProps> = ({
  onCardClick,
  className = "",
}) => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const appRef = useRef<Application | null>(null);
  const cardsRef = useRef<ExtendedSprite[]>([]);
  const isCarouselMovedRef = useRef(false);
  const isMovingRef = useRef(false);
  const rotationOffsetRef = useRef({ value: 0 });

  const carouselOpacityRef = useRef<number>(1);
  const textContainerRef = useRef<HTMLDivElement | null>(null);
  const carouselRef = useRef<HTMLDivElement | null>(null);

  const updateCardsRef = useRef<() => void>(() => {});
  const pointerDownHandlerRef = useRef<(event: PointerEvent) => void>();
  const pointerUpHandlerRef = useRef<(event: PointerEvent) => void>();
  const pointerMoveHandlerRef = useRef<(event: PointerEvent) => void>();

  const moveCards = (distance: number) => {
    cardsRef.current.forEach((card) => {
      if (!card.userData.yOffset) {
        card.userData.yOffset = 0;
      }
      card.userData.yOffset += distance;
    });
    updateCardsRef.current();
  };

  // Carousel을 아래로 이동시키는 함수
  const moveCardsDown = (distance: number) => {
    if (isMovingRef.current) {
      return;
    }
    isMovingRef.current = true;
    moveCards(distance);
    isCarouselMovedRef.current = true;

    // useRef로 관리된 carouselOpacityRef와 DOM 직접 조작
    carouselOpacityRef.current = 0.25;
    if (carouselRef.current) {
      carouselRef.current.style.opacity = "0.25";
    }

    if (textContainerRef.current) {
      textContainerRef.current.style.display = "none";
    }

    isMovingRef.current = false;
  };

  // Carousel을 원래 위치로 되돌리는 함수
  const moveCardsUp = (distance: number) => {
    if (isMovingRef.current) {
      return;
    }
    isMovingRef.current = true;
    moveCards(-distance);
    isCarouselMovedRef.current = false;

    carouselOpacityRef.current = 1;
    if (carouselRef.current) {
      carouselRef.current.style.opacity = "1";
    }
    // 텍스트 복원
    if (textContainerRef.current) {
      textContainerRef.current.style.display = "block";
    }

    isMovingRef.current = false;
  };

  // 카드 클릭 이벤트 핸들러
  const handleCardClick = (videoUrl: string) => {
    if (isMovingRef.current) {
      return;
    }
    if (isCarouselMovedRef.current) {
      moveCardsUp(430);
    } else {
      onCardClick(videoUrl);
      moveCardsDown(430);
    }
  };

  const items = [
    { imageUrl: "/assets/CircleCarousel/베테랑.jpg", videoUrl: "BQZ3-mdczwM" },
    { imageUrl: "/assets/CircleCarousel/기생충.jpg", videoUrl: "ke5YikykPj0" },
    { imageUrl: "/assets/CircleCarousel/노트북.jpg", videoUrl: "nTTK0S0LPtk" },
    { imageUrl: "/assets/CircleCarousel/듄.jpg", videoUrl: "ZQhuVLPXf24" },
    { imageUrl: "/assets/CircleCarousel/명량.jpg", videoUrl: "spQtwggaCy4" },
    {
      imageUrl: "/assets/CircleCarousel/반지의제왕.jpg",
      videoUrl: "VfrSYFChe40",
    },
    {
      imageUrl: "/assets/CircleCarousel/보헤미안랩소디.jpg",
      videoUrl: "ARCwA5NVcOM",
    },
    {
      imageUrl: "/assets/CircleCarousel/분노의질주.jpg",
      videoUrl: "1-Q0a2a_jcs",
    },
    {
      imageUrl: "/assets/CircleCarousel/서울의봄.jpg",
      videoUrl: "-AZ7cnwn2YI",
    },
    { imageUrl: "/assets/CircleCarousel/신세계.jpg", videoUrl: "rvLMVcRkRfY" },
    { imageUrl: "/assets/CircleCarousel/암살.jpg", videoUrl: "OkEZTCPpgXc" },
    {
      imageUrl: "/assets/CircleCarousel/어바웃타임.jpg",
      videoUrl: "ksn1zUkcKys",
    },
    {
      imageUrl: "/assets/CircleCarousel/어벤저스.jpg",
      videoUrl: "ijUsSpRVhBU",
    },
    {
      imageUrl: "/assets/CircleCarousel/올드보이.jpg",
      videoUrl: "2HkjrJ6IK5E",
    },
    { imageUrl: "/assets/CircleCarousel/인셉션.jpg", videoUrl: "Uj7z5HH9nfs" },
    {
      imageUrl: "/assets/CircleCarousel/콰이어트플레이스.jpg",
      videoUrl: "rf7SFaFSf5c",
    },
    {
      imageUrl: "/assets/CircleCarousel/타이타닉.jpg",
      videoUrl: "9KQm_7Lpt5E",
    },
    { imageUrl: "/assets/CircleCarousel/파묘.jpg", videoUrl: "fRkOWmfZjkY" },
    { imageUrl: "/assets/CircleCarousel/베놈.jpg", videoUrl: "BryJA-Xp-Q4" },
    {
      imageUrl: "/assets/CircleCarousel/극한직업.jpg",
      videoUrl: "-OvSJ4_zc2c",
    },
  ];

  useEffect(() => {
    const app = new Application();
    appRef.current = app;

    // Function to load textures
    const loadTexture = async (url: string): Promise<Texture> => {
      try {
        if (!url) throw new Error("No URL provided");
        const texture = await Assets.load(url);
        return texture || Texture.WHITE;
      } catch (error) {
        console.error("Failed to load texture:", error);
        return Texture.WHITE;
      }
    };

    const initializeApp = async () => {
      await app.init({
        autoStart: true,
        resizeTo: window,
        backgroundColor: 0x000000,
        sharedTicker: false,
        autoDensity: true,
        resolution: window.devicePixelRatio || 1,
      });

      if (pixiContainerRef.current) {
        pixiContainerRef.current.appendChild(app.view);

        const centerX = app.screen.width / 2;
        const centerY = app.screen.height;
        const radiusX = 750;
        const radiusY = 400;
        const cardCount = 20;
        const cards: ExtendedSprite[] = [];
        const spacingFactor = 1;

        // Load textures
        const textures = await Promise.all(
          items.map((item) => loadTexture(item.imageUrl))
        );

        // Function to create card background texture
        const createCardBackgroundTexture = (
          width: number,
          height: number,
          borderColor: string,
          borderWidth: number,
          borderRadius: number
        ) => {
          const canvas = document.createElement("canvas");
          canvas.width = width;
          canvas.height = height;
          const ctx = canvas.getContext("2d");

          if (ctx) {
            // Function to draw a rounded rectangle
            const drawRoundedRect = (
              ctx: CanvasRenderingContext2D,
              x: number,
              y: number,
              width: number,
              height: number,
              radius: number
            ) => {
              ctx.beginPath();
              ctx.moveTo(x + radius, y);
              ctx.lineTo(x + width - radius, y);
              ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
              ctx.lineTo(x + width, y + height - radius);
              ctx.quadraticCurveTo(
                x + width,
                y + height,
                x + width - radius,
                y + height
              );
              ctx.lineTo(x + radius, y + height);
              ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
              ctx.lineTo(x, y + radius);
              ctx.quadraticCurveTo(x, y, x + radius, y);
              ctx.closePath();
            };

            // Draw the filled rounded rectangle for the background
            ctx.fillStyle = "#ffffff";
            drawRoundedRect(
              ctx,
              borderWidth / 2,
              borderWidth / 2,
              width - borderWidth,
              height - borderWidth,
              borderRadius
            );
            ctx.fill();

            // Draw the border around the rounded rectangle
            ctx.lineWidth = borderWidth;
            ctx.strokeStyle = borderColor;
            drawRoundedRect(
              ctx,
              borderWidth / 2,
              borderWidth / 2,
              width - borderWidth,
              height - borderWidth,
              borderRadius
            );
            ctx.stroke();
          }

          return Texture.from(canvas);
        };

        // Create card background texture with rounded corners
        const backgroundTexture = createCardBackgroundTexture(
          220,
          310,
          "#000000",
          4,
          8
        );

        // Function to set up each card
        function setupCard(
          texture: Texture,
          backgroundTexture: Texture,
          index: number,
          videoUrl: string
        ) {
          const cardBackground = new Sprite(backgroundTexture);
          cardBackground.anchor.set(0.5);

          const sprite = new Sprite(texture || Texture.WHITE);
          sprite.width = 220;
          sprite.height = 310;
          sprite.position.set(-110, -155);

          // Create a mask to round the corners of the sprite
          const maskGraphics = new Graphics();
          maskGraphics.beginFill(0xffffff);
          maskGraphics.drawRoundedRect(-110, -155, 220, 310, 8);
          maskGraphics.endFill();

          // Apply the mask to the sprite
          sprite.mask = maskGraphics;

          const cardContainer = new Sprite() as ExtendedSprite;
          cardContainer.addChild(cardBackground);
          cardContainer.addChild(sprite);
          cardContainer.addChild(maskGraphics);

          const angle = (index / cardCount) * Math.PI * 2 * spacingFactor;
          cardContainer.x = centerX + radiusX * Math.cos(angle);
          cardContainer.y = centerY + radiusY * Math.sin(angle);

          cardContainer.rotation = 0;
          cardContainer.userData = { angle, rotationOffset: 0, yOffset: 0 };
          cardContainer.interactive = true;
          cardContainer.cursor = "pointer";
          cardContainer.on("pointertap", () => {
            handleCardClick(videoUrl);
          });

          cards.push(cardContainer);
          cardsRef.current.push(cardContainer);
          app.stage.addChild(cardContainer);
        }

        // Set up all cards
        for (let i = 0; i < cardCount; i++) {
          const item = items[i % items.length];
          const texture = textures[i % textures.length];
          setupCard(texture, backgroundTexture, i, item.videoUrl);
        }

        let dragging = false;
        let previousPosition = { x: 0, y: 0 };

        // Update cards function
        const updateCards = () => {
          const rotationOffset = rotationOffsetRef.current.value;
          cards.forEach((card, i) => {
            if (card) {
              const angle =
                (i / cardCount) * Math.PI * 2 * spacingFactor + rotationOffset;

              const baseY = centerY + radiusY * Math.sin(angle);
              const yOffset = card.userData.yOffset || 0;
              card.x = centerX + radiusX * Math.cos(angle);
              card.y = baseY + yOffset;
              card.rotation = 0;
              card.userData.angle = angle;
            }
          });

          if (appRef.current) {
            appRef.current.renderer.render(appRef.current.stage);
          }
        };
        updateCardsRef.current = updateCards;

        const rotateAnimation = () => {
          gsap.to(rotationOffsetRef.current, {
            value: Math.PI * 2, // 한 바퀴 회전
            duration: 2,
            ease: "power2.inOut",
            onUpdate: () => {
              updateCards();
            },
            onComplete: () => {
              setupMouseEvents();
            },
          });
        };

        // Event handlers
        const setupMouseEvents = () => {
          const pointerDownHandler = (event: PointerEvent) => {
            dragging = true;
            previousPosition = { x: event.clientX, y: event.clientY };
          };
          pointerDownHandlerRef.current = pointerDownHandler;

          const pointerUpHandler = () => {
            dragging = false;
          };
          pointerUpHandlerRef.current = pointerUpHandler;

          const pointerMoveHandler = (event: PointerEvent) => {
            if (!dragging) return;

            const currentPosition = { x: event.clientX, y: event.clientY };
            const deltaX = currentPosition.x - previousPosition.x;

            // 마우스 드래그에 따라 회전 값 조정
            rotationOffsetRef.current.value += deltaX * 0.002;
            updateCards();

            previousPosition = currentPosition;
          };
          pointerMoveHandlerRef.current = pointerMoveHandler;

          // Add event listeners
          app.view.addEventListener("pointerdown", pointerDownHandler);
          app.view.addEventListener("pointerup", pointerUpHandler);
          app.view.addEventListener("pointermove", pointerMoveHandler);
        };

        rotateAnimation();
        app.ticker.add(updateCards);
        app.start();
      }
    };

    initializeApp();

    // Cleanup function
    return () => {
      // Remove event listeners
      if (
        appRef.current &&
        appRef.current.view &&
        pointerDownHandlerRef.current &&
        pointerUpHandlerRef.current &&
        pointerMoveHandlerRef.current
      ) {
        appRef.current.view.removeEventListener(
          "pointerdown",
          pointerDownHandlerRef.current
        );
        appRef.current.view.removeEventListener(
          "pointerup",
          pointerUpHandlerRef.current
        );
        appRef.current.view.removeEventListener(
          "pointermove",
          pointerMoveHandlerRef.current
        );
      }

      // Remove ticker function
      if (appRef.current && updateCardsRef.current) {
        appRef.current.ticker.remove(updateCardsRef.current);
      }

      // Destroy the Pixi application
      if (appRef.current) {
        appRef.current.destroy(true, {
          children: true,
          texture: true,
        });
        appRef.current = null;
      }

      if (Assets && Assets.cache) {
        Assets.cache.reset();
      }
    };
  }, []);

  return (
    <div className="relative w-full h-full">
      {/* 
        기존에는 carouselOpacity === 1 조건으로 이 문구를 렌더링했지만,
        이제는 DOM을 직접 조작할 예정이므로 항상 렌더링해두고
        display를 토글합니다.
      */}
      <div
        ref={textContainerRef}
        className="fixed top-1/4 left-1/2 transform -translate-x-1/2 -translate-y-1/2"
        style={{
          color: "#fff",
          zIndex: 10,
          pointerEvents: "none",
        }}
      >
        <p className="text-[57px] italic bg-gradient-to-r from-gray-400 via-white to-gray-400 text-transparent bg-clip-text pr-2">
          FIND YOUR OWN TASTE !
        </p>
      </div>

      <div
        ref={(el) => {
          pixiContainerRef.current = el;
          carouselRef.current = el; // 동일 요소 참조
        }}
        className={`w-full h-full ${className} rounded-md`}
        style={{
          position: "absolute",
          top: "50%",
          transform: "translateY(-50%)",
          pointerEvents: "auto",
          opacity: carouselOpacityRef.current,
        }}
      ></div>
    </div>
  );
};

export default CircleCarousel;
