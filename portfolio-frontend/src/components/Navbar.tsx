import { Link, useLocation } from "react-router-dom";

function Navbar() {
  // Make the navbar fixed on the homepage
  const isFixed = useLocation().pathname === "/";
  return (
    <nav className={`navbar py-7 px-11 fs-3xl ${isFixed ? "fixed-top" : "sticky-top"}`}>
      <div className="align-items-center d-flex gap-10">
        <Link className="nav-icon" to="/">
          <i className="bi bi-house" />
        </Link>
        <Link className="nav-link" to="/projects">
          Projects
        </Link>
        <Link className="nav-link" to="/resume">
          Resume
        </Link>
      </div>
      <a className="nav-icon" href="https://github.com/emsidhu">
        <i className="bi bi-github"></i>
      </a>
    </nav>
  );
}
export default Navbar;
