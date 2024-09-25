import React, { useEffect, useRef, useState } from "react";
import { Application, Sprite, Assets, Texture } from "pixi.js";
import { ExtendedSprite } from "../type";

interface CircleCarouselProps {
  onCardClick: (videoUrl: string) => void; // 외부에서 사용하는 함수
  className?: string;
}

const CircleCarousel: React.FC<CircleCarouselProps> = ({
  onCardClick,
  className = "",
}) => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const appRef = useRef<Application | null>(null);
  const cardsRef = useRef<ExtendedSprite[]>([]); // 카드들을 참조하기 위한 ref
  const isCarouselMovedRef = useRef(false); // useRef로 상태 관리
  const isMovingRef = useRef(false); // 이동 중 여부 상태

  // Refs for cleanup
  const updateCardsRef = useRef<() => void>(() => {});
  const pointerDownHandlerRef = useRef<(event: PointerEvent) => void>();
  const pointerUpHandlerRef = useRef<(event: PointerEvent) => void>();
  const pointerMoveHandlerRef = useRef<(event: PointerEvent) => void>();
  const [carouselOpacity, setCarouselOpacity] = useState(1);

  const moveCards = (distance: number) => {
    cardsRef.current.forEach((card) => {
      if (!card.userData.yOffset) {
        card.userData.yOffset = 0; // yOffset 초기화
      }
      card.userData.yOffset += distance; // yOffset 증가 또는 감소
      // console.log(
      //   `After Move - Card ${index}: yOffset = ${card.userData.yOffset}`
      // );
    });
    updateCardsRef.current(); // 위치 갱신
  };

  // Carousel을 아래로 이동시키는 함수
  const moveCardsDown = (distance: number) => {
    if (isMovingRef.current) {
      return; // 이동 중일 때는 다른 이동 방지
    }
    isMovingRef.current = true; // 이동 중 상태 설정
    moveCards(distance);
    isCarouselMovedRef.current = true; // 이동 완료 후 상태 업데이트
    setCarouselOpacity(0.25);
    isMovingRef.current = false; // 이동 완료 후 이동 상태 해제
    console.log("moveCardsDown: Carousel moved down");
  };

  // Carousel을 원래 위치로 되돌리는 함수
  const moveCardsUp = (distance: number) => {
    if (isMovingRef.current) {
      return; // 이동 중일 때는 다른 이동 방지
    }
    isMovingRef.current = true; // 이동 중 상태 설정
    moveCards(-distance); // 음수로 이동
    isCarouselMovedRef.current = false; // 원래 위치로 돌아온 상태 설정
    setCarouselOpacity(1);
    isMovingRef.current = false; // 이동 완료 후 이동 상태 해제
    console.log("moveCardsUp: Carousel moved up");
  };

  // 카드 클릭 이벤트 핸들러
  const handleCardClick = (videoUrl: string) => {
    if (isMovingRef.current) {
      return; // 이동 중에는 클릭 이벤트 무시
    }
    if (isCarouselMovedRef.current) {
      moveCardsUp(430); // 원래 위치로 되돌림
    } else {
      onCardClick(videoUrl); // 비디오 재생 이벤트 발생
      moveCardsDown(430); // Carousel을 아래로 이동
    }
  };

  const items = [
    {
      imageUrl: "/assets/CircleCarousel/베테랑.jpg",
      videoUrl: "BQZ3-mdczwM",
    },
    {
      imageUrl: "/assets/CircleCarousel/기생충.jpg",
      videoUrl: "ke5YikykPj0",
    },
    {
      imageUrl: "/assets/CircleCarousel/노트북.jpg",
      videoUrl: "nTTK0S0LPtk",
    },
    {
      imageUrl: "/assets/CircleCarousel/듄.jpg",
      videoUrl: "ZQhuVLPXf24",
    },
    {
      imageUrl: "/assets/CircleCarousel/명량.jpg",
      videoUrl: "spQtwggaCy4",
    },
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
    {
      imageUrl: "/assets/CircleCarousel/신세계.jpg",
      videoUrl: "rvLMVcRkRfY",
    },
    {
      imageUrl: "/assets/CircleCarousel/암살.jpg",
      videoUrl: "OkEZTCPpgXc",
    },
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
    {
      imageUrl: "/assets/CircleCarousel/인셉션.jpg",
      videoUrl: "Uj7z5HH9nfs",
    },
    {
      imageUrl: "/assets/CircleCarousel/콰이어트플레이스.jpg",
      videoUrl: "rf7SFaFSf5c",
    },
    {
      imageUrl: "/assets/CircleCarousel/타이타닉.jpg",
      videoUrl: "9KQm_7Lpt5E",
    },
    {
      imageUrl: "/assets/CircleCarousel/파묘.jpg",
      videoUrl: "fRkOWmfZjkY",
    },
    {
      imageUrl: "/assets/CircleCarousel/베놈.jpg",
      videoUrl: "BryJA-Xp-Q4",
    },
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
        const radiusX = 750; // Horizontal radius
        const radiusY = 400; // Vertical radius
        const cardCount = 20; // Number of cards
        const cards: ExtendedSprite[] = [];
        const spacingFactor = 1; // Card spacing adjustment
        let lastClosestCard: ExtendedSprite | null = null;

        // Load textures
        const textures = await Promise.all(
          items.map((item) => loadTexture(item.imageUrl))
        );

        // Function to create card background texture
        const createCardBackgroundTexture = (
          width: number,
          height: number,
          borderColor: string,
          borderWidth: number
        ) => {
          const canvas = document.createElement("canvas");
          canvas.width = width;
          canvas.height = height;
          const ctx = canvas.getContext("2d");

          if (ctx) {
            ctx.fillStyle = "#ffffff";
            ctx.fillRect(
              borderWidth,
              borderWidth,
              width - borderWidth * 2,
              height - borderWidth * 2
            );
            ctx.lineWidth = borderWidth;
            ctx.strokeStyle = borderColor;
            ctx.strokeRect(
              borderWidth / 2,
              borderWidth / 2,
              width - borderWidth,
              height - borderWidth
            );
          }

          return Texture.from(canvas);
        };

        // Create card background texture
        const backgroundTexture = createCardBackgroundTexture(
          220,
          310,
          "#000000",
          4
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

          const cardContainer = new Sprite() as ExtendedSprite;
          cardContainer.addChild(cardBackground);
          cardContainer.addChild(sprite);

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
          cardsRef.current.push(cardContainer); // cardsRef.current에 추가
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
        let rotationOffset = 0;

        // Function to calculate angle
        function calculateAngle(
          x1: number,
          y1: number,
          x2: number,
          y2: number
        ) {
          return Math.atan2(y2 - y1, x2 - x1);
        }

        // Update cards function
        const updateCards = () => {
          cards.forEach((card, i) => {
            if (card) {
              const angle =
                (i / cardCount) * Math.PI * 2 * spacingFactor + rotationOffset;

              // 기본 원형 배열 위치
              const baseY = centerY + radiusY * Math.sin(angle);

              // moveCardsDown에서 추가된 yOffset을 반영
              const yOffset = card.userData.yOffset || 0;

              // 최종 y 위치 계산
              card.x = centerX + radiusX * Math.cos(angle);
              card.y = baseY + yOffset; // 원래 위치에 yOffset 추가

              card.rotation = 0;
              card.userData.angle = angle;
            }
          });

          bringCenterCardToFront();

          if (appRef.current) {
            appRef.current.renderer.render(appRef.current.stage); // Pixi.js 화면 갱신
          }
        };
        updateCardsRef.current = updateCards; // Store reference for cleanup

        // Function to bring the center card to the front
        function bringCenterCardToFront() {
          let closestCard: ExtendedSprite | null = null;
          let minAngleDiff = Infinity;

          cards.forEach((card) => {
            const angleDiff = Math.abs(card.userData.angle - Math.PI / 2);
            if (angleDiff < minAngleDiff) {
              minAngleDiff = angleDiff;
              closestCard = card;
            }
          });

          if (
            closestCard &&
            closestCard !== lastClosestCard &&
            minAngleDiff < 0.01
          ) {
            lastClosestCard = closestCard;
            app.stage.setChildIndex(closestCard, app.stage.children.length - 1);
          }
        }

        // Event handlers
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
          const prevAngle = calculateAngle(
            previousPosition.x,
            previousPosition.y,
            centerX,
            centerY
          );
          const currentAngle = calculateAngle(
            currentPosition.x,
            currentPosition.y,
            centerX,
            centerY
          );

          const angleDiff = currentAngle - prevAngle;
          rotationOffset += angleDiff;

          previousPosition = currentPosition;

          updateCards();
        };
        pointerMoveHandlerRef.current = pointerMoveHandler;

        // Add event listeners
        app.view.addEventListener("pointerdown", pointerDownHandler);
        app.view.addEventListener("pointerup", pointerUpHandler);
        app.view.addEventListener("pointermove", pointerMoveHandler);

        // Use app.ticker instead of Ticker.shared
        app.ticker.add(updateCards);

        // Start the application
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
      <div
        ref={pixiContainerRef}
        className={`w-full h-full ${className}`}
        style={{
          position: "absolute",
          top: "50%",
          transform: "translateY(-50%)",
          pointerEvents: "auto", // 이벤트를 받을 수 있도록 설정
          opacity: carouselOpacity,
        }}
      ></div>
    </div>
  );
};

export default CircleCarousel;
