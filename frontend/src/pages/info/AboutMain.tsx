// import React, { useEffect } from "react";
// import "../info/Aboutmain.css";
// import Matter from "matter-js"; // Matter.js import

// const splitWords = () => {
//   const textNode = document.querySelector(".text");
//   if (!textNode) return;

//   const text = textNode.textContent;
//   const newDomElements = text.split(" ").map((word) => {
//     const highlighted =
//       word.startsWith(`"Flicker"`) || word.startsWith(`Mythrill`);
//     return `<span class="word ${
//       highlighted ? "highlighted" : ""
//     }">${word}</span>`;
//   });
//   textNode.innerHTML = newDomElements.join("");
// };

// const renderCanvas = () => {
//   const { Engine, Render, World, Bodies, Runner, Mouse, MouseConstraint } =
//     Matter;

//   const params = {
//     isStatic: true,
//     render: {
//       fillStyle: "transparent",
//     },
//   };
//   const canvasSize = {
//     width: window.innerWidth,
//     height: window.innerHeight,
//   };

//   const engine = Engine.create({});
//   const render = Render.create({
//     element: document.body,
//     engine: engine,
//     options: {
//       ...canvasSize,
//       background: "transparent",
//       wireframes: false,
//     },
//   });

//   const floor = Bodies.rectangle(
//     canvasSize.width / 2,
//     canvasSize.height,
//     canvasSize.width,
//     50,
//     params
//   );
//   const wall1 = Bodies.rectangle(
//     0,
//     canvasSize.height / 2,
//     50,
//     canvasSize.height,
//     params
//   );
//   const wall2 = Bodies.rectangle(
//     canvasSize.width,
//     canvasSize.height / 2,
//     50,
//     canvasSize.height,
//     params
//   );
//   const top = Bodies.rectangle(
//     canvasSize.width / 2,
//     0,
//     canvasSize.width,
//     50,
//     params
//   );

//   const wordElements = document.querySelectorAll(".word");
//   const wordBodies = [...wordElements].map((elemRef) => {
//     const width = elemRef.offsetWidth;
//     const height = elemRef.offsetHeight;

//     return {
//       body: Bodies.rectangle(canvasSize.width / 2, 0, width, height, {
//         render: {
//           fillStyle: "transparent",
//         },
//       }),
//       elem: elemRef,
//       render() {
//         const { x, y } = this.body.position;
//         this.elem.style.top = `${y - 20}px`;
//         this.elem.style.left = `${x - width / 2}px`;
//         this.elem.style.transform = `rotate(${this.body.angle}rad)`;
//       },
//     };
//   });

//   const mouse = Mouse.create(document.body);
//   const mouseConstraint = MouseConstraint.create(engine, {
//     mouse,
//     constraint: {
//       stiffness: 0.2,
//       render: {
//         visible: false,
//       },
//     },
//   });

//   World.add(engine.world, [
//     floor,
//     ...wordBodies.map((box) => box.body),
//     wall1,
//     wall2,
//     top,
//     mouseConstraint,
//   ]);

//   render.mouse = mouse;
//   Runner.run(engine);
//   Render.run(render);

//   (function rerender() {
//     wordBodies.forEach((element) => {
//       element.render();
//     });
//     Matter.Engine.update(engine);
//     requestAnimationFrame(rerender);
//   })();
// };

// const AboutMain = () => {
//   useEffect(() => {
//     splitWords();
//     renderCanvas();
//   }, []);

//   return (
//     <div>
//       <div className="text">
//         software developer with over 9 years of experience, I have developed a
//         strong foundation in crafting innovative and efficient technology
//         solutions. My passion for technology and entrepreneurship led me to
//         co-found Mythrill, where I currently serve as the CTO. I am proud to be
//         recognized as one of the "Flicker" Armenians in Tech and am constantly
//         driven to push boundaries and make a positive impact in the industry.
//         When I'm not coding, I enjoy exploring my creative side through art,
//         music,
//       </div>
//     </div>
//   );
// };

// export default AboutMain;
