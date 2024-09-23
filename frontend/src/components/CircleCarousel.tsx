import React, { useEffect, useRef } from "react";
import { Application, Sprite, Assets, Texture } from "pixi.js";
import { useNavigate } from "react-router-dom";

interface ExtendedSprite extends Sprite {
  userData: {
    angle: number;
    rotationOffset: number;
  };
}

const CircleCarousel: React.FC = () => {
  const pixiContainerRef = useRef<HTMLDivElement | null>(null);
  const appRef = useRef<Application | null>(null);
  const navigate = useNavigate();

  // Refs for cleanup
  const updateCardsRef = useRef<() => void>(() => {});
  const pointerDownHandlerRef = useRef<(event: PointerEvent) => void>();
  const pointerUpHandlerRef = useRef<(event: PointerEvent) => void>();
  const pointerMoveHandlerRef = useRef<(event: PointerEvent) => void>();

  const imageUrls = [
    "/assets/survey/image1.jpg",
    "/assets/survey/image2.jpg",
    "/assets/survey/image3.jpg",
    "/assets/survey/image20.jpg",
    "/assets/survey/image5.jpg",
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
        autoStart: true, // Start the application immediately
        resizeTo: window,
        backgroundColor: 0x000000,
        sharedTicker: false, // Use a dedicated ticker for this app
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
          imageUrls.map((url) => loadTexture(url))
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
          index: number
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
          cardContainer.userData = { angle, rotationOffset: 0 };
          cardContainer.interactive = true;
          cardContainer.cursor = "pointer";
          cardContainer.on("pointertap", () => {
            navigate("/moviedetail");
          });

          cards.push(cardContainer);
          app.stage.addChild(cardContainer);
        }

        // Set up all cards
        for (let i = 0; i < cardCount; i++) {
          const texture = textures[i % textures.length]; // Use loaded textures
          setupCard(texture, backgroundTexture, i);
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
              card.x = centerX + radiusX * Math.cos(angle);
              card.y = centerY + radiusY * Math.sin(angle);

              card.rotation = 0;
              card.userData.angle = angle;
            }
          });

          bringCenterCardToFront();
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
            console.log("Closest card detected, moving to top.");
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
          baseTexture: true,
        });
        appRef.current = null;
      }

      // Optionally clear the assets cache
      // Correct method to clear cache
      if (Assets && Assets.cache) {
        Assets.cache.reset();
      }
    };
  }, [navigate]);

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
