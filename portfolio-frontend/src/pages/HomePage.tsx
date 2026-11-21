import { useEffect } from "react";
import { Link } from "react-router-dom";

function Home() {
  useEffect(() => {
    document.body.classList.add("home-body");

    return () => {
      document.body.classList.remove("home-body");
    };
  }, []);
  return (
    <div className="min-h-screen flex flex-col text-center justify-center items-center px-4">
      <p className="font-work-sans text-6xl font-extrabold">Hi, I'm Ekam Sidhu.</p>
      <p className="font-work-sans text-2xl mt-2 font-bold">
        Welcome to my corner of the web. <br />
        Explore my latest projects and what I'm building next.
      </p>
      <Link
        to="/projects"
        className="font-bold bg-purple-500/25 border-2 border-purple-500 text-2xl rounded-md py-1.5 px-3 mt-4"
      >
        View my work
      </Link>
    </div>
  );
}
export default Home;
