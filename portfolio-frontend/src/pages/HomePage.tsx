import { useEffect } from "react";

function Home() {
  useEffect(() => {
    document.body.classList.add("home-body");

    return () => {
      document.body.classList.remove("home-body");
    };
  }, []);
  return (
    <div className="vh-100 d-flex flex-column text-center justify-content-center align-items-center">
      <p className="work-sans fs-6xl">Hi, I'm Ekam Sidhu.</p>
      <p className="work-sans fs-xl">
        Welcome to my corner of the web. <br />
        Explore my latest projects and what I'm building next.
      </p>
      <a href="/projects" className="btn home-btn">
        View my work
      </a>
    </div>
  );
}
export default Home;
