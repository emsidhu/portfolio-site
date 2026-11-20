import { useEffect } from "react";

function Home() {
  useEffect(() => {
    document.body.classList.add("home-body");

    return () => {
      document.body.classList.remove("home-body");
    };
  }, []);
  return (
    <div className="min-h-screen flex flex-col text-center justify-center items-center px-4">
      <p className="font-work-sans text-6xl">Hi, I'm Ekam Sidhu.</p>
      <p className="font-work-sans text-xl mt-2">
        Welcome to my corner of the web. <br />
        Explore my latest projects and what I'm building next.
      </p>
      <a href="/projects" className="home-btn">
        View my work
      </a>
    </div>
  );
}
export default Home;
