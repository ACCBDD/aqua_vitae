#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 FocusCenter;
uniform int PlayerBAC;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);

    float effectRadius = 0.1;
    vec2 dir = texCoord - FocusCenter;
    float dist = dot(dir, dir);
    float clampedDist = clamp(dist / effectRadius, 0.0, 1.0);


    float radius = float(PlayerBAC / 40000) * clampedDist;

    if (radius <= 1.0) {
        fragColor = base;
        return;
    }

    float texelSizeX = 1.0 / InSize.x;
    vec4 colorAcc = vec4(0.0);
    float totalWeight = 0.0;

    for (float x = -radius; x <= radius; x += 1.0) {
        colorAcc += texture(DiffuseSampler, vec2(texCoord.x + (x * texelSizeX), texCoord.y));
        totalWeight += 1.0;
    }

    fragColor = colorAcc / totalWeight;
}
