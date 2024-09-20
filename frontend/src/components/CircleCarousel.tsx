import React, { useEffect, useRef } from "react";
import { Application, Sprite, Ticker, Assets, Texture } from "pixi.js";

// ExtendedSprite 타입 정의
interface ExtendedSprite extends Sprite {
  userData: {
    angle: number;
    rotationOffset: number;
  };
}

const CircleCarousel: React.FC = () => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const appRef = useRef<Application | null>(null);

  const imageUrls = [
    "/assets/survey/image1.jpg",
    "/assets/survey/image2.jpg",
    "/assets/survey/image3.jpg",
    "/assets/survey/image20.jpg",
    "/assets/survey/image5.jpg",
  ];

  useEffect(() => {
    const app = new Application();

    const initializeApp = async () => {
      await app.init({
        autoStart: false,
        resizeTo: window,
        backgroundColor: 0x000000,
        sharedTicker: true,
        autoDensity: true,
        resolution: window.devicePixelRatio || 1,
      });

      if (pixiContainerRef.current) {
        pixiContainerRef.current.appendChild(app.view);
        appRef.current = app;

        const centerX = app.screen.width / 2;
        const centerY = app.screen.height;
        const radiusX = 750; // 가로 반지름
        const radiusY = 400; // 세로 반지름
        const cardCount = 20; // 카드 개수
        const cards: ExtendedSprite[] = [];
        const spacingFactor = 1; // 카드 간격 조절
        let lastClosestCard: ExtendedSprite | null = null;

        // 텍스처 로드 함수
        const loadTexture = async (url: string) => {
          try {
            if (!url) throw new Error("No URL provided");
            const texture = await Assets.load(url);
            return texture || Texture.WHITE;
          } catch (error) {
            console.error("Failed to load texture:", error);
            return Texture.WHITE;
          }
        };

        // 카드 배경 텍스처 생성 함수
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

        // 카드 배경 텍스처 생성
        const backgroundTexture = createCardBackgroundTexture(
          220,
          310,
          "#000000",
          4
        );

        // 카드 생성
        for (let i = 0; i < cardCount; i++) {
          const texture = await loadTexture(imageUrls[i % imageUrls.length]); // 이미지 URL로 텍스처 로드
          setupCard(
            texture,
            backgroundTexture,
            i,
            centerX,
            centerY,
            radiusX,
            radiusY,
            spacingFactor
          );
        }

        // 카드 설정 함수
        function setupCard(
          texture: Texture,
          backgroundTexture: Texture,
          index: number,
          centerX: number,
          centerY: number,
          radiusX: number,
          radiusY: number,
          spacingFactor: number
        ) {
          const cardBackground = new Sprite(backgroundTexture);
          cardBackground.anchor.set(0.5);

          // 수정된 부분: 카드 설정 함수 내 이미지의 크기와 위치를 카드 배경에 맞게 조정
          const sprite = new Sprite(texture || Texture.WHITE);
          sprite.width = 220; // 배경의 너비에 맞춤
          sprite.height = 310; // 배경의 높이에 맞춤
          sprite.position.set(-110, -155); // 배경의 중심에 맞게 위치 조정

          const cardContainer = new Sprite() as ExtendedSprite;
          cardContainer.addChild(cardBackground);
          cardContainer.addChild(sprite);

          // 간격 조절을 위해 spacingFactor를 적용하여 각도를 계산
          const angle = (index / cardCount) * Math.PI * 2 * spacingFactor;
          cardContainer.x = centerX + radiusX * Math.cos(angle);
          cardContainer.y = centerY + radiusY * Math.sin(angle);

          // 모든 카드를 정면으로 바라보도록 회전 각도 0으로 설정
          cardContainer.rotation = 0;

          cardContainer.userData = { angle, rotationOffset: 0 };

          cards.push(cardContainer);
          app.stage.addChild(cardContainer);
        }

        let dragging = false;
        let previousPosition = { x: 0, y: 0 };
        let rotationOffset = 0;

        // 각도 계산 함수
        function calculateAngle(
          x1: number,
          y1: number,
          x2: number,
          y2: number
        ) {
          return Math.atan2(y2 - y1, x2 - x1);
        }

        // 카드 업데이트 함수
        function updateCards() {
          cards.forEach((card, i) => {
            const angle =
              (i / cardCount) * Math.PI * 2 * spacingFactor + rotationOffset;
            card.x = centerX + radiusX * Math.cos(angle);
            card.y = centerY + radiusY * Math.sin(angle);

            // 모든 카드를 정면으로 바라보도록 회전 각도 0으로 유지
            card.rotation = 0;

            card.userData.angle = angle;
          });

          bringCenterCardToFront();
        }

        // 중심에 가장 가까운 카드를 최상단으로 이동
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

          // 가장 가까운 카드가 변경되었고, 중앙에 정확히 왔을 때만 최상단 이동
          if (
            closestCard &&
            closestCard !== lastClosestCard &&
            minAngleDiff < 0.01
          ) {
            console.log("Closest card detected, moving to top.");
            lastClosestCard = closestCard;
            app.stage.setChildIndex(closestCard, app.stage.children.length - 1);
          }
        }

        // 드래그 이벤트 설정
        app.view.addEventListener("pointerdown", (event) => {
          dragging = true;
          previousPosition = { x: event.clientX, y: event.clientY };
        });

        app.view.addEventListener("pointerup", () => {
          dragging = false;
        });

        app.view.addEventListener("pointermove", (event) => {
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

          updateCards(); // 카드 업데이트 및 최상단 카드 처리
        });

        // 티커를 통해 애플리케이션 렌더링
        Ticker.shared.add(() => {
          updateCards(); // 매 프레임마다 카드 업데이트 및 최상단 카드 처리
          app.render();
        });
      }
    };

    // 초기화 함수 호출
    initializeApp();

    return () => {
      if (Ticker.shared) {
        Ticker.shared.stop();
        Ticker.shared.destroy();
      }

      if (appRef.current) {
        appRef.current.destroy(true, { children: true, texture: true });
      }
    };
  }, []);

  return (
    <div
      ref={pixiContainerRef}
      className="w-full h-full"
      style={{
        position: "absolute",
        top: "50%",
        transform: "translateY(-50%)",
      }}
    />
  );
};

export default CircleCarousel;
