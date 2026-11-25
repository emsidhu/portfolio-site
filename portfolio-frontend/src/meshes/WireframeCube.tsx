import { Line } from "@react-three/drei";
import { useFrame } from "@react-three/fiber";
import type { ThreeElements } from "@react-three/fiber";
import { useRef } from "react";
import { BoxGeometry, Color, EdgesGeometry } from "three";
import type { Group } from "three";

const { points, vertexColors } = (() => {
  const box = new BoxGeometry(2, 2, 2);
  const geometry = new EdgesGeometry(box);
  box.dispose();

  const positions = geometry.getAttribute("position");
  const points: [number, number, number][] = [];
  const vertexColors: Color[] = [];
  const firstColor = new Color("#c084fc");
  const secondColor = new Color("#67e8f9");

  for (let i = 0; i < positions.count; i += 1) {
    const [x, y, z] = [positions.getX(i), positions.getY(i), positions.getZ(i)];
    const isEven = (Number(x > 0) + Number(y > 0) + Number(z > 0)) % 2 === 0;
    points.push([x, y, z]);
    vertexColors.push(isEven ? firstColor : secondColor);
  }

  geometry.dispose();
  return { points, vertexColors };
})();

function WireframeCube(props: ThreeElements["group"]) {
  const cube = useRef<Group | null>(null);

  // Rotate cube
  useFrame(() => {
    if (cube.current) {
      cube.current.rotation.x += 0.001;
      cube.current.rotation.y += 0.0015;
    }
  });

  return (
    <group {...props} ref={cube}>
      {/* Thicker black lines to outline the gradient */}
      <Line points={points} segments color="#111827" lineWidth={20} depthWrite={false} />

      <Line points={points} segments vertexColors={vertexColors} lineWidth={15} />
    </group>
  );
}
export default WireframeCube;
