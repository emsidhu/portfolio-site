import { useEffect, useRef, useState } from "react";
import { Link, useLocation } from "react-router-dom";

function Navbar() {
  const [isFixed, setIsFixed] = useState(false);
  const headerRef = useRef(null);

  // Makes the navbar position absolute on the homepage
  const isOnHomepage = useLocation().pathname === "/";

  // Start navbar animation when header leaves the screen
  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        setIsFixed(!entry.isIntersecting);
      },
      {
        root: null,
        // make it wait a little before nav comes down
        rootMargin: "150px 0px 0px 0px",
        threshold: 0,
      },
    );

    if (headerRef.current) {
      observer.observe(headerRef.current);
    }

    return () => observer.disconnect();
  }, []);

  // Change positioning (and background) based on header visibility and whether it's on the homepage
  const positionClasses = isFixed
    ? "fixed top-0 left-0 right-0 z-50 bg-white/90 backdrop-blur-md shadow-sm animate__animated animate__fadeInDown animate__faster"
    : isOnHomepage
      ? "absolute top-0 left-0 right-0 z-50 bg-transparent"
      : "relative z-50 bg-transparent";

  return (
    /* Keep space reserved on non-homepage routes so content doesn't jump when nav fixes */
    <header ref={headerRef} className={isOnHomepage ? "" : "h-23"}>
      <nav
        className={`flex items-center justify-between font-semibold py-7 px-11 text-3xl ${positionClasses}`}
      >
        <div className="flex items-center gap-10">
          <Link className="nav-icon" to="/">
            <i className="bi bi-house" />
          </Link>
          <Link className="nav-link" to="/projects">
            Projects
          </Link>
        </div>
        <a className="nav-icon" href="https://github.com/emsidhu">
          <i className="bi bi-github"></i>
        </a>
      </nav>
    </header>
  );
}
export default Navbar;
