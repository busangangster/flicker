import { useEffect } from "react";
import { useLocation } from "react-router-dom";

export default function ScrollToTop(props: { children: unknown }) {
  const { pathname } = useLocation();

  useEffect(() => {
    const scrollContainer = document.querySelector(".scrollable-container"); // 스크롤이 일어나는 컨테이너
    if (scrollContainer) {
      scrollContainer.scrollTo(0, 0); // 해당 컨테이너를 최상단으로 이동
    } else {
      window.scrollTo(0, 0); // 기본적으로 window를 최상단으로 이동
    }
  }, [pathname]);

  return <>{props.children}</>;
}
