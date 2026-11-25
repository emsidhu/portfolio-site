import { useFrame } from "@react-three/fiber";
import type { ThreeElements } from "@react-three/fiber";
import { useRef } from "react";
import type { Mesh } from "three";

function Cube(props: ThreeElements["mesh"]) {
  const cube = useRef<Mesh | null>(null);

  useFrame(() => {
    if (cube.current) {
      cube.current.rotation.x += 0.002;
      cube.current.rotation.y += 0.003;
    }
  });
  return (
    <mesh {...props} ref={cube}>
      <boxGeometry args={[2, 2, 2]} />
      <meshStandardMaterial color={"white"} />
    </mesh>
  );
}
export default Cube;
