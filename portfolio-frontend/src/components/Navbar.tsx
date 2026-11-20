function Navbar() {
  return (
    <nav className="navbar fixed-top fs-3xl">
      <div className="align-items-center d-flex gap-10">
        <a className="nav-icon" href="/">
          <i className="bi bi-house" />
        </a>
        <a className="nav-link" href="/projects">
          Projects
        </a>
        <a className="nav-link" href="/resume">
          Resume
        </a>
      </div>
      <a className="nav-icon" href="https://github.com/emsidhu">
        <i className="bi bi-github"></i>
      </a>
    </nav>
  );
}
export default Navbar;
