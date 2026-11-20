import { Link, useLocation } from "react-router-dom";

function Navbar() {
  // Make the navbar fixed on the homepage
  const isFixed = useLocation().pathname === "/";
  return (
    <nav
      className={`flex items-center justify-between font-semibold bg-transparent py-7 px-11 text-3xl ${
        isFixed ? "fixed top-0 left-0 right-0 z-50" : "sticky top-0 z-50"
      }`}
    >
      <div className="flex items-center gap-10">
        <Link className="text-5xl" to="/">
          <i className="bi bi-house" />
        </Link>
        <Link to="/projects">Projects</Link>
        <Link to="/resume">Resume</Link>
      </div>
      <a className="text-5xl" href="https://github.com/emsidhu">
        <i className="bi bi-github"></i>
      </a>
    </nav>
  );
}
export default Navbar;
