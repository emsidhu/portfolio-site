import { useEffect } from "react";
import { Link } from "react-router-dom";
import { Canvas } from "@react-three/fiber";
import { OrbitControls, PerspectiveCamera } from "@react-three/drei";
import WireframeCube from "../meshes/WireframeCube";

function Home() {
  useEffect(() => {
    document.body.classList.add("home-body");

    return () => {
      document.body.classList.remove("home-body");
    };
  }, []);
  return (
    <div className="relative min-h-screen">
      <div className="absolute inset-0 z-0">
        <Canvas>
          <PerspectiveCamera makeDefault position={[0, 0, 10]} />

          <WireframeCube scale={3} />
          <OrbitControls makeDefault />
        </Canvas>
      </div>
      <div className="relative z-10 flex min-h-screen flex-col items-center justify-center px-4 text-center [text-shadow:0_2px_6px_rgba(0,0,0,0.65)] pointer-events-none">
        <p className="font-work-sans text-6xl font-extrabold">Hi, I'm Ekam Sidhu.</p>
        <p className="font-work-sans text-2xl mt-2 font-bold">
          Welcome to my corner of the web. <br />
          Explore my latest projects and what I'm building next.
        </p>
        <Link
          to="/projects"
          className="pointer-events-auto font-bold bg-purple-500/25 border-2 border-purple-500 text-2xl rounded-md py-1.5 px-3 mt-4"
        >
          View my work
        </Link>
      </div>
    </div>
  );
}

export default Home;
