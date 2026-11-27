import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Canvas } from "@react-three/fiber";
import { OrbitControls, PerspectiveCamera } from "@react-three/drei";
import { Bloom, EffectComposer } from "@react-three/postprocessing";
import WireframeCube from "../meshes/WireframeCube";

function Home() {
  const phrases = ["Software Developer.", "Full-Stack.", "Java and React.", "Web Apps."];
  const [phraseIndex, setPhraseIndex] = useState(0);
  const [phraseVisible, setPhraseVisible] = useState(true);

  useEffect(() => {
    const timeout = window.setTimeout(
      () => {
        if (phraseVisible) {
          setPhraseVisible(false);
        } else {
          setPhraseIndex((index) => (index + 1) % phrases.length);
          setPhraseVisible(true);
        }
      },
      phraseVisible ? 1500 : 500,
    );

    return () => window.clearTimeout(timeout);
  }, [phraseVisible, phrases.length]);

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
          <EffectComposer>
            <Bloom
              mipmapBlur
              luminanceThreshold={0.25}
              luminanceSmoothing={0.2}
              intensity={0.7}
              radius={0.6}
            />
          </EffectComposer>
        </Canvas>
      </div>
      <div className="relative z-10 grid min-h-screen grid-rows-[1fr_auto_1fr] justify-items-center px-4 text-center [text-shadow:0_2px_6px_rgba(0,0,0,0.65)] pointer-events-none">
        <div className="row-start-2 flex flex-col items-center self-center">
          <p className="font-work-sans text-[clamp(3.5rem,11vw+1rem,9rem)] font-extrabold">
            EKAM SIDHU
          </p>
          <p
            className={`font-work-sans text-5xl mt-2 transition-opacity duration-500 motion-reduce:transition-none ${
              phraseVisible ? "opacity-100" : "opacity-0"
            }`}
          >
            {phrases[phraseIndex]}
          </p>
        </div>

        <Link
          to="/projects"
          className="pointer-events-auto row-start-3 mt-10 self-start rounded-lg border border-cyan-100/70 bg-linear-to-r from-cyan-300/15 to-violet-300/20 px-3 py-1.5 text-4xl text-white shadow-[0_0_18px_rgba(196,181,253,0.18)] transition-colors duration-200 hover:border-violet-100 hover:from-cyan-300/25 hover:to-violet-300/30 focus-visible:outline focus-visible:outline-offset-4 focus-visible:outline-cyan-100 motion-reduce:transition-none"
        >
          View my work
        </Link>
      </div>
    </div>
  );
}

export default Home;
