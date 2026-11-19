function Navbar() {
  return (
    <nav className="navbar d-flex flex-row pb-3">
      <a className="navbar-brand" href="/">
        <i className="bi bi-house" />
      </a>
      <a href="/projects">Projects</a>
      <a href="/resume">Resume</a>
      <a href="https://github.com/emsidhu">
        <i className="bi bi-github"></i>
      </a>
    </nav>
  );
}
export default Navbar;
